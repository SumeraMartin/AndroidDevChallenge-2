package com.example.androiddevchallenge.coreactor

import com.example.androiddevchallenge.coreactor.action.Action
import com.example.androiddevchallenge.coreactor.reducer.Reducer
import com.example.androiddevchallenge.coreactor.contract.state.State
import com.example.androiddevchallenge.coreactor.internal.log.Logger
import com.example.androiddevchallenge.coreactor.lifecycle.LifecycleState

class ConsoleLogger<STATE : State>(private val tag: String) : CoreactorLogger<STATE> {

    override fun onState(state: STATE) {
        log("State: $state")
    }

    override fun onStateDispatchedToView(state: STATE) {
        log("State dispatched: $state")
    }

    override fun onAction(action: Action<STATE>) {
        log("Action: $action")
    }

    override fun onLifecycle(lifecycleState: LifecycleState) {
        log("Lifecycle: $lifecycleState")
    }

    override fun onReducer(oldState: STATE, reducer: Reducer<STATE>, newState: STATE) {
        log("Reducer <<<<<<")
        log("Reducer old state: $oldState")
        log("Reducer: $reducer")
        log("Reducer new state: $newState")
        log("Reducer >>>>>>")
    }

    private fun log(message: String) {
        Logger.log(tag, message)
    }
}
