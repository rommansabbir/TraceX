package com.rommansabbir.loggerx

object LoggerXProvider {
    private val impl by lazy { LoggerXImpl() }

    /*Initialize LoggerX*/
    fun register(config: LoggerXConfig) {
        impl.init(config)
    }

    /*Return LoggerX instance*/
    val INSTANCE: LoggerX
        get() = impl
}