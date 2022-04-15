package com.rommansabbir.tracex

object TraceXProvider {
    private val impl by lazy { TraceXImpl() }

    /*Initialize LoggerX*/
    fun register(config: TraceXConfig) {
        impl.init(config)
    }

    /*Return LoggerX instance*/
    val INSTANCE: TraceX
        get() = impl
}