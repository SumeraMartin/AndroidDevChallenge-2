package com.example.androiddevchallenge.coreactor.internal.assert

import android.os.Looper
import com.example.androiddevchallenge.coreactor.error.CoreactorException

internal object CoreactorMainThreadChecker {

    var ignoreCheck = false

    fun requireMainThread(methodName: String) {
        if (ignoreCheck) {
            return
        }
        if (Looper.getMainLooper().thread != Thread.currentThread()) {
            throw CoreactorException("$methodName method is not called on the main thread")
        }
    }
}

fun requireMainThread(methodName: String) {
    CoreactorMainThreadChecker.requireMainThread(methodName)
}
