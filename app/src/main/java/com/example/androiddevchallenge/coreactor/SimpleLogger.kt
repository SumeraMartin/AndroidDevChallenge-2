package com.example.androiddevchallenge.coreactor

import com.example.androiddevchallenge.coreactor.action.Action
import com.example.androiddevchallenge.coreactor.reducer.Reducer
import com.example.androiddevchallenge.coreactor.contract.state.State
import com.example.androiddevchallenge.coreactor.lifecycle.LifecycleState

open class SimpleLogger<STATE : State> : CoreactorLogger<STATE> {

    override fun onState(state: STATE) {
        // NoOp
    }

    override fun onStateDispatchedToView(state: STATE) {
        // NoOp
    }

    override fun onAction(action: Action<STATE>) {
        // NoOp
    }

    override fun onLifecycle(lifecycleState: LifecycleState) {
        // NoOp
    }

    override fun onReducer(oldState: STATE, reducer: Reducer<STATE>, newState: STATE) {
        // NoOp
    }
}
