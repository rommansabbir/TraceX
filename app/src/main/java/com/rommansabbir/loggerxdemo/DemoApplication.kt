package com.rommansabbir.loggerxdemo

import android.app.Application
import com.rommansabbir.loggerx.LoggerXImpl

class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val logger = LoggerXImpl()
        logger.register(this)
    }
}