package com.example.androiddevchallenge.coreactor

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.coreactor.internal.assert.requireMainThread
import com.example.androiddevchallenge.coreactor.error.CoreactorException
import com.example.androiddevchallenge.coreactor.lifecycle.LifecycleState
import com.example.androiddevchallenge.coreactor.reducer.Reducer
import com.example.androiddevchallenge.coreactor.action.Action
import com.example.androiddevchallenge.coreactor.contract.reducer.reducer
import com.example.androiddevchallenge.coreactor.interceptor.CoreactorInterceptor
import com.example.androiddevchallenge.coreactor.contract.state.State
import com.example.androiddevchallenge.coreactor.interceptor.impl.SimpleInterceptor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

abstract class CoreactorViewModel<STATE : State, ACTION : Action<STATE>> : ViewModel(), LifecycleObserver, CoroutineScope by MainScope() {

    init {
        onCreate()
    }

    val actionReceiver: (ACTION) -> Unit = { action -> dispatchAction(action) }

    fun dispatchAction(action: ACTION) {
        requireMainThread("dispatchAction")

        launch {
//            if (lifecycleState.isInitialState) {
//                throw CoreactorException("dispatchAction shouldn't be called before TODO")
//            }
            if (lifecycleState.isDetachState) {
                throw CoreactorException("dispatchAction shouldn't be called after onCleared")
            }

            actionHandler.dispatchAction(action)
        }
    }

    val stateFlow: StateFlow<STATE> get() = stateHandler.stateFlow

    protected abstract val initialState: STATE

    protected abstract fun onAction(action: ACTION)

    protected open val logger: CoreactorLogger<STATE> = SimpleLogger()

    protected open val interceptor: CoreactorInterceptor<STATE, ACTION> = SimpleInterceptor()

    protected val state: STATE get() = stateHandler.currentState

    protected val lifecycleState: LifecycleState get() = lifecycleStateHandler.currentState

    protected val actionsFlow: SharedFlow<ACTION> get() = actionHandler.actionsFlow

    protected val reducersFlow: SharedFlow<Reducer<STATE>> get() = reducerHandler.reducersFlow

    protected val lifecycleFlow: StateFlow<LifecycleState> get() = lifecycleStateHandler.stateFlow

    private val stateHandler = StateHandler { initialState }

    private val actionHandler = ActionHandler()

    private val reducerHandler = ReducerHandler()

    private val lifecycleStateHandler = LifecycleStateHandler()

    private val scopedJobsDispatcher = ScopedJobsDispatcher()

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    protected fun onAny(@Suppress("UNUSED_PARAMETER") source: LifecycleOwner, event: Lifecycle.Event) {
        runBlocking {
            val lifecycleState = LifecycleState.fromLifecycle(event)
            lifecycleStateHandler.dispatchLifecycleState(lifecycleState)

            if (lifecycleState.isCreateState) {
                onCreate()
            }
        }
    }
    final override fun onCleared() {
        runBlocking {
            lifecycleStateHandler.dispatchLifecycleState(LifecycleState.ON_DETACH)
            cancelActiveCoroutines()
        }
    }

    //region Protected methods
    protected open fun onState(state: STATE) {
        // NoOp
    }

    protected open fun onLifecycleState(state: LifecycleState) {
        // NoOp
    }

    protected open fun onCreate() {
        // NoOp
    }

    protected fun launchWhenResumed(block: suspend () -> Unit) {
        requireMainThread("launchWhenResumed")
        scopedJobsDispatcher.startOrWaitUntilResumedState(block)
    }

    protected fun launchWhenStarted(block: suspend () -> Unit) {
        requireMainThread("launchWhenStarted")
        scopedJobsDispatcher.startOrWaitUntilStartedState(block)
    }

    protected fun launchWhenCreated(block: suspend () -> Unit) {
        requireMainThread("launchWhenCreated")
        scopedJobsDispatcher.startOrWaitUntilCreatedState(block)
    }

    protected fun onLifecycleException(error: Throwable) {
        throw error
    }

    protected suspend fun emitReducer(block: (STATE) -> STATE) {
        requireMainThread("emit")
        reducerHandler.dispatchReducer(reducer(block))
    }

    protected suspend fun emit(block: () -> Reducer<STATE>) {
        requireMainThread("emit")
        reducerHandler.dispatchReducer(block())
    }

    protected suspend fun emit(reducer: Reducer<STATE>) {
        requireMainThread("emit")
        reducerHandler.dispatchReducer(reducer)
    }

    protected suspend fun waitUntilState(predicate: (STATE) -> Boolean): STATE {
        return waitUntil(stateFlow, predicate)
    }

    protected suspend inline fun <reified EXPECTED_REDUCER : Reducer<STATE>> waitUntilReducerType(): EXPECTED_REDUCER {
        return waitUntil(reducersFlow) { it is EXPECTED_REDUCER } as? EXPECTED_REDUCER ?: throw CoreactorException()
    }

    protected suspend fun waitUntilReducer(predicate: (Reducer<STATE>) -> Boolean): Reducer<STATE> {
        return waitUntil(reducersFlow, predicate)
    }

    protected suspend inline fun <reified EXPECTED_ACTION : ACTION> waitUntilActionType(): EXPECTED_ACTION {
        return waitUntilAction { it is EXPECTED_ACTION } as? EXPECTED_ACTION ?: throw CoreactorException()
    }

    protected suspend fun waitUntilAction(predicate: (ACTION) -> Boolean): ACTION {
        return waitUntil(actionsFlow, predicate)
    }

    protected suspend fun waitUntilLifecycle(lifecycleState: LifecycleState): LifecycleState {
        return waitUntilLifecycle { it == lifecycleState }
    }

    protected suspend fun waitUntilLifecycle(predicate: (LifecycleState) -> Boolean): LifecycleState {
        return waitUntil(lifecycleFlow, predicate)
    }

    protected suspend fun <VALUE> waitUntil(flow: Flow<VALUE>, predicate: (VALUE) -> Boolean): VALUE {
        requireMainThread("waitUntil")
        return flow.filter { predicate(it) }.first()
    }
    //endregion

    private fun cancelActiveCoroutines() {
        cancel("The coreactor is being destroyed.")
    }

    private inner class StateHandler(lazyInitialState: () -> STATE) {

        private val mutableStateFlowInternal by lazy { MutableStateFlow(lazyInitialState()) }

        val currentState: STATE get() {
            return mutableStateFlowInternal.value
        }

        val stateFlow: StateFlow<STATE> get() {
            return mutableStateFlowInternal.asStateFlow()
        }

        suspend fun dispatchState(state: STATE) {
            val interceptedState = interceptor.onInterceptState(state) ?: return
            logger.onState(interceptedState)
            mutableStateFlowInternal.emit(interceptedState)
        }
    }

    private inner class ActionHandler {

        private val actionsSharedFlowInternal = MutableSharedFlow<ACTION>()

        val actionsFlow: SharedFlow<ACTION> get() {
            return actionsSharedFlowInternal.asSharedFlow()
        }

        suspend fun dispatchAction(action: ACTION) {
            val interceptedAction = interceptor.onInterceptAction(action) ?: return
            logger.onAction(interceptedAction)
            actionsSharedFlowInternal.emit(interceptedAction)
            onAction(interceptedAction)
        }
    }

    private inner class ReducerHandler {

        private val reducerSharedFlowInternal = MutableSharedFlow<Reducer<STATE>>()

        val reducersFlow: SharedFlow<Reducer<STATE>> get() {
            return reducerSharedFlowInternal.asSharedFlow()
        }

        suspend fun dispatchReducer(reducer: Reducer<STATE>) {
            val interceptedReducer = interceptor.onInterceptReducer(reducer) ?: return
            val oldState = stateHandler.currentState
            val newState = interceptedReducer.reduce(oldState)
            logger.onReducer(oldState, interceptedReducer, newState)
            reducerSharedFlowInternal.emit(interceptedReducer)
            stateHandler.dispatchState(newState)
        }
    }

    private inner class LifecycleStateHandler {

        private val lifecycleStateFlowInternal = MutableStateFlow(LifecycleState.INITIAL)

        val currentState: LifecycleState get() {
            return lifecycleStateFlowInternal.value
        }

        val stateFlow: StateFlow<LifecycleState> get() {
            return lifecycleStateFlowInternal.asStateFlow()
        }

        suspend fun dispatchLifecycleState(state: LifecycleState) {
            interceptor.onLifecycleStateChanged(state)
            logger.onLifecycle(state)
            lifecycleStateFlowInternal.emit(state)
            scopedJobsDispatcher.onLifecycleState(lifecycleState)
        }
    }

    private inner class ScopedJobsDispatcher {

        private val jobsWaitingForCreatedState = mutableListOf<Job>()

        private val jobsWaitingForStartedState = mutableListOf<Job>()

        private val jobsWaitingForResumedState = mutableListOf<Job>()

        private val jobsRunningUntilPausedState = mutableListOf<Job>()

        private val jobsRunningUntilStoppedState = mutableListOf<Job>()

        private val jobsRunningUntilDestroyedState = mutableListOf<Job>()

        fun startOrWaitUntilResumedState(block: suspend () -> Unit) {
            val job = toLazyJob(block)
            if (lifecycleState.isInResumedState) {
                startJobAndAddToRunningJobs(job, cancelWhen = LifecycleState.ON_PAUSE)
            } else {
                waitUntilExpectedState(job, startWhen = LifecycleState.ON_RESUME)
            }
        }

        fun startOrWaitUntilStartedState(block: suspend () -> Unit) {
            val job = toLazyJob(block)
            if (lifecycleState.isInStartedState) {
                startJobAndAddToRunningJobs(job, cancelWhen = LifecycleState.ON_STOP)
            } else {
                waitUntilExpectedState(job, startWhen = LifecycleState.ON_START)
            }
        }

        fun startOrWaitUntilCreatedState(block: suspend () -> Unit) {
            val job = toLazyJob(block)
            if (lifecycleState.isInCreatedState) {
                startJobAndAddToRunningJobs(job, cancelWhen = LifecycleState.ON_DESTROY)
            } else {
                waitUntilExpectedState(job, startWhen = LifecycleState.ON_CREATE)
            }
        }

        fun onLifecycleState(state: LifecycleState) {
            when (state) {
                LifecycleState.ON_CREATE -> {
                    startAllWaitingJobs(
                        forState = LifecycleState.ON_CREATE,
                        cancelWhen = LifecycleState.ON_DESTROY
                    )
                }
                LifecycleState.ON_START -> {
                    startAllWaitingJobs(
                        forState = LifecycleState.ON_START,
                        cancelWhen = LifecycleState.ON_STOP
                    )
                }
                LifecycleState.ON_RESUME -> {
                    startAllWaitingJobs(
                        forState = LifecycleState.ON_RESUME,
                        cancelWhen = LifecycleState.ON_PAUSE
                    )
                }
                LifecycleState.ON_PAUSE -> {
                    cancelAllRunningJobs(forState = LifecycleState.ON_PAUSE)
                }
                LifecycleState.ON_STOP -> {
                    cancelAllRunningJobs(forState = LifecycleState.ON_STOP)
                }
                LifecycleState.ON_DESTROY -> {
                    cancelAllRunningJobs(forState = LifecycleState.ON_DESTROY)
                }
                else -> {
                    // NoOp
                }
            }
        }

        private fun startAllWaitingJobs(forState: LifecycleState, cancelWhen: LifecycleState) {
            getWaitingJobsListFor(forState).apply {
                forEach { job ->
                    startJobAndAddToRunningJobs(job, cancelWhen)
                }
                clear()
            }
        }

        private fun cancelAllRunningJobs(forState: LifecycleState) {
            getRunningJobsListFor(forState).apply {
                forEach { job ->
                    job.cancel(CancellationException("Coreactor has been stopped"))
                }
                clear()
            }
        }

        private fun startJobAndAddToRunningJobs(job: Job, cancelWhen: LifecycleState) {
            job.start()
            getRunningJobsListFor(cancelWhen).add(job)
        }

        private fun waitUntilExpectedState(job: Job, startWhen: LifecycleState) {
            getWaitingJobsListFor(startWhen).add(job)
        }

        private fun getWaitingJobsListFor(lifecycleState: LifecycleState): MutableList<Job> {
            return when (lifecycleState) {
                LifecycleState.ON_CREATE -> jobsWaitingForCreatedState
                LifecycleState.ON_START -> jobsWaitingForStartedState
                LifecycleState.ON_RESUME -> jobsWaitingForResumedState
                else -> throw CoreactorException("Unexpected lifecycle state: $lifecycleState")
            }
        }

        private fun getRunningJobsListFor(lifecycleState: LifecycleState): MutableList<Job> {
            return when (lifecycleState) {
                LifecycleState.ON_PAUSE -> jobsRunningUntilPausedState
                LifecycleState.ON_STOP -> jobsRunningUntilStoppedState
                LifecycleState.ON_DESTROY -> jobsRunningUntilDestroyedState
                else -> throw CoreactorException("Unexpected lifecycle state: $lifecycleState")
            }
        }

        private fun toLazyJob(block: suspend () -> Unit): Job {
            return launch(start = CoroutineStart.LAZY) { block() }
        }
    }
}
