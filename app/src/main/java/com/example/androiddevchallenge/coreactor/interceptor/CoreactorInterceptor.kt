package com.example.androiddevchallenge.coreactor.interceptor

import com.example.androiddevchallenge.coreactor.lifecycle.LifecycleState
import com.example.androiddevchallenge.coreactor.action.Action
import com.example.androiddevchallenge.coreactor.reducer.Reducer
import com.example.androiddevchallenge.coreactor.contract.state.State

interface CoreactorInterceptor<STATE : State, ACTION : Action<STATE>> {

    fun onInterceptState(state: STATE): STATE?

    fun onInterceptAction(action: ACTION): ACTION?

    fun onInterceptReducer(reducer: Reducer<STATE>): Reducer<STATE>?

    fun onLifecycleStateChanged(lifecycleState: LifecycleState)
}
