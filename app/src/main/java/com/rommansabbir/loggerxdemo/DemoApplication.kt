package com.rommansabbir.loggerxdemo

import android.app.Application
import com.rommansabbir.loggerx.LoggerXConfig
import com.rommansabbir.loggerx.LoggerXProvider

class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LoggerXProvider.register(LoggerXConfig(this, false))
    }
}