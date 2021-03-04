package com.example.androiddevchallenge.coreactor.interceptor.impl

import com.example.androiddevchallenge.coreactor.lifecycle.LifecycleState
import com.example.androiddevchallenge.coreactor.action.Action
import com.example.androiddevchallenge.coreactor.reducer.Reducer
import com.example.androiddevchallenge.coreactor.contract.state.State
import com.example.androiddevchallenge.coreactor.interceptor.CoreactorInterceptor

open class SimpleInterceptor<STATE : State, ACTION : Action<STATE>> : CoreactorInterceptor<STATE, ACTION> {

    override fun onInterceptState(state: STATE): STATE? {
        return state
    }

    override fun onInterceptAction(action: ACTION): ACTION? {
        return action
    }

    override fun onInterceptReducer(reducer: Reducer<STATE>): Reducer<STATE>? {
        return reducer
    }

    override fun onLifecycleStateChanged(lifecycleState: LifecycleState) {
        // NoOp
    }
}