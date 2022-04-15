package com.rommansabbir.tracexdemo

import android.app.Application
import com.rommansabbir.tracex.TraceXConfig
import com.rommansabbir.tracex.TraceXProvider

class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TraceXProvider.register(TraceXConfig(this, true))
        val logs = TraceXProvider.INSTANCE.getRecentCrashLogs()
        val s = "Last Crash Logs: ${logs.size}"
        println(s)
    }
}