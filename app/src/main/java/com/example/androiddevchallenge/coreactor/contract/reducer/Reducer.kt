package com.example.androiddevchallenge.coreactor.reducer

import com.example.androiddevchallenge.coreactor.contract.state.State

abstract class Reducer<STATE: State> {

    abstract fun reduce(state: STATE): STATE
}
