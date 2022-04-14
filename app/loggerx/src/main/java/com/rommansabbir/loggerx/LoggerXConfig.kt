package com.rommansabbir.loggerx

import android.app.Application

data class LoggerXConfig(
    val application: Application,
    val crashOnRuntimeException: Boolean
)