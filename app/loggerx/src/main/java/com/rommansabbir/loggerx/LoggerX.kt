package com.rommansabbir.loggerx

import android.app.Activity
import android.app.Application
import android.os.Bundle

interface LoggerX {
    fun register(application: Application): Boolean
}

class LoggerXImpl : LoggerX {
    // Callback for activity lifecycle for this specific application
    private val activityCallback = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            println("created")
            activity.apply {
                Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
                    println(throwable.message)
                }
            }
        }

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {}

    }

    override fun register(application: Application): Boolean {
        application.registerActivityLifecycleCallbacks(activityCallback)
        return true
    }
}