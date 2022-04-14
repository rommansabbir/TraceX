package com.rommansabbir.loggerx

import androidx.activity.ComponentActivity
import com.rommansabbir.loggerx.model.DeviceInfo

fun ComponentActivity.registerForLoggerX(callback: LoggerXCallback?) {
    LoggerXProvider.INSTANCE.registerListener(callback)
}

fun ComponentActivity.registerForLoggerX(callback: (deviceInfo: DeviceInfo, thread: Thread, throwable: Throwable) -> Unit = { _, _, _ -> }) {
    LoggerXProvider.INSTANCE.registerListener(
        object : LoggerXCallback {
            override fun onEvent(deviceInfo: DeviceInfo, thread: Thread, throwable: Throwable) {
                callback.invoke(deviceInfo, thread, throwable)
            }
        }
    )
}


