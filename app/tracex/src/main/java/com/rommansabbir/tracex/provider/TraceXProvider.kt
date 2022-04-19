package com.rommansabbir.tracex.provider

import com.rommansabbir.tracex.TraceX
import com.rommansabbir.tracex.config.TraceXConfig
import com.rommansabbir.tracex.impl.TraceXImpl

object TraceXProvider {
    private val impl by lazy { TraceXImpl() }

    /*Initialize TraceX*/
    fun register(config: TraceXConfig) {
        impl.init(config)
    }

    /*Return TraceX instance*/
    val INSTANCE: TraceX
        get() = impl
}