package com.rommansabbir.tracex

import android.app.Application

data class TraceXConfig(
    val application: Application,
    val autoRegisterForEachActivity: Boolean
)