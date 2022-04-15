package com.rommansabbir.tracex

import com.rommansabbir.tracex.model.DeviceInfo

interface TraceXCallback {
    fun onEvent(
        deviceInfo: DeviceInfo,
        thread: Thread,
        throwable: Throwable,
        processKiller: ProcessKiller
    )
}