package com.rommansabbir.tracex

import android.app.Activity
import com.rommansabbir.tracex.exception.TraceXNotInitializedException
import com.rommansabbir.tracex.model.TraceXCrashLog

interface TraceX {
    /*Initialize LoggerX*/
    fun init(config: TraceXConfig): Boolean

    /*Register to get notified regarding UncaughtException events*/
    fun registerListener(listener: TraceXCallback?)

    fun registerActivity(activity: Activity?): Boolean

    @Throws(TraceXNotInitializedException::class)
    fun reportLog(throwable: Throwable, additionalInfo: String = ""): Boolean

    @Throws(TraceXNotInitializedException::class)
    fun getRecentCrashLogs(): MutableList<TraceXCrashLog>

    @Throws(TraceXNotInitializedException::class)
    fun clearCrashLogs(list: MutableList<TraceXCrashLog>)

    @Throws(TraceXNotInitializedException::class)
    fun clearAllLogs(): Boolean
}


