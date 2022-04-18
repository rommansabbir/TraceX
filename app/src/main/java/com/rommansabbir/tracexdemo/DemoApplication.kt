package com.rommansabbir.tracexdemo

import android.app.Application
import com.rommansabbir.tracex.config.TraceXConfig
import com.rommansabbir.tracex.provider.TraceXProvider

class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        /*Initialize TraceX with TraceXConfig*/
        TraceXProvider.register(TraceXConfig(this,
            autoRegisterForEachActivity = true,
            autoLogRuntimeExceptions = true
        ))
    }
}