package com.rommansabbir.tracex.impl

import android.app.Activity
import com.rommansabbir.tracex.TraceXCallback
import com.rommansabbir.tracex.config.TraceXConfig
import com.rommansabbir.tracex.model.TraceXCrashLog


internal class TraceXImpl : BaseTraceXImpl() {

    override fun init(config: TraceXConfig): Boolean {
        return baseInit(config)
    }

    override fun registerListener(listener: TraceXCallback?) {
        checkInitialization()
        this.callback = listener
    }

    override fun registerActivity(activity: Activity?): Boolean {
        checkInitialization()
        return registerActivityBase(activity)
    }

    override fun writeANewLog(throwable: Throwable, additionalInfo: String): Boolean {
        checkInitialization()
        return writeLogToCacheDir(throwable, additionalInfo)
    }

    override fun getRecentCrashLogs(): MutableList<TraceXCrashLog> {
        checkInitialization()
        return getRecentCrashLogsBase()
    }

    override fun clearCrashLogs(list: MutableList<TraceXCrashLog>) {
        checkInitialization()
        clearCrashLogsBase(list)
    }

    override fun clearAllLogs(): Boolean {
        checkInitialization()
        clearAllLogsBase()
        return true
    }
}