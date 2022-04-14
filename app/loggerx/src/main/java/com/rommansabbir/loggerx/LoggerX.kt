package com.rommansabbir.loggerx

interface LoggerX {
    /*Initialize LoggerX*/
    fun init(config: LoggerXConfig): Boolean

    /*Register to get notified regarding UncaughtException events*/
    fun registerListener(listener: LoggerXCallback?)
}


