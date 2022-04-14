package com.rommansabbir

import androidx.activity.ComponentActivity
import com.rommansabbir.loggerx.LoggerXCallback
import com.rommansabbir.loggerx.LoggerXProvider

fun ComponentActivity.registerForLoggerX(callback: LoggerXCallback?) {
    LoggerXProvider.INSTANCE.registerListener(callback)
}


