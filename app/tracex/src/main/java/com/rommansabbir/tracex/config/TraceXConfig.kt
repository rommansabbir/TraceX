package com.rommansabbir.tracex.config

import android.app.Application

/**
 * Configuration class to initialize [TraceX].
 *
 * @param application [Application] instance.
 * @param autoRegisterForEachActivity To auto register every activity when created.
 * @param autoLogRuntimeExceptions automatically create a encrypted log  of [RuntimeException]s.
 */
data class TraceXConfig(
    val application: Application,
    val autoRegisterForEachActivity: Boolean,
    val autoLogRuntimeExceptions: Boolean
)