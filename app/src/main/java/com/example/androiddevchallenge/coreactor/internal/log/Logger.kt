package com.example.androiddevchallenge.coreactor.internal.log

import com.example.androiddevchallenge.coreactor.internal.log.impl.ConsoleLogger

object Logger {

    interface Writer {
        fun logMessage(tag: String, message: String)
    }

    var writer: Writer = ConsoleLogger()

    fun log(tag: String, message: String) {
        writer.logMessage(tag, message)
    }
}
