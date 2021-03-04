package com.example.androiddevchallenge.coreactor

import com.example.androiddevchallenge.coreactor.lifecycle.LifecycleState
import com.example.androiddevchallenge.coreactor.action.Action
import com.example.androiddevchallenge.coreactor.reducer.Reducer
import com.example.androiddevchallenge.coreactor.contract.state.State

interface CoreactorLogger<STATE : State> {

    fun onState(state: STATE)

    fun onStateDispatchedToView(state: STATE)

    fun onAction(action: Action<STATE>)

    fun onLifecycle(lifecycleState: LifecycleState)

    fun onReducer(oldState: STATE, reducer: Reducer<STATE>, newState: STATE)
}
