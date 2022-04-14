package com.rommansabbir.loggerx

import com.rommansabbir.loggerx.model.DeviceInfo

interface LoggerXCallback {
    fun onEvent(deviceInfo: DeviceInfo, thread: Thread, throwable: Throwable)
}