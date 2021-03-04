package com.example.androiddevchallenge.coreactor.contract.reducer

import com.example.androiddevchallenge.coreactor.contract.state.State
import com.example.androiddevchallenge.coreactor.reducer.Reducer

fun <STATE : State> reducer(reducerAction: (STATE) -> STATE): Reducer<STATE> {
    return object : Reducer<STATE>() {
        override fun reduce(state: STATE): STATE  = reducerAction(state)
    }
}
