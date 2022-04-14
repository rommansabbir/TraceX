package com.rommansabbir.loggerx

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.os.Bundle
import com.rommansabbir.loggerx.model.DeviceInfo


internal class LoggerXImpl : LoggerX {
    private var config: LoggerXConfig? = null
    private var callback: LoggerXCallback? = null

    @SuppressLint("HardwareIds")
    private fun getSystemDetail(contentResolver: ContentResolver): DeviceInfo =
        DeviceInfo.Builder.build(contentResolver)


    // Callback for activity lifecycle for this specific application
    private val activityCallback = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            /*Register Thread.setDefaultUncaughtExceptionHandler for every activity by default*/
            activity.apply {
                Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
                    callback?.onEvent(
                        getSystemDetail(
                            this.contentResolver
                        ), thread,
                        throwable
                    )
                    if ((config?.crashOnRuntimeException == true) && throwable is RuntimeException) {
                        finish()
                    }
                }
            }
        }

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
        }

    }

    override fun init(config: LoggerXConfig): Boolean {
        this.config = config
        this.config!!.application.registerActivityLifecycleCallbacks(activityCallback)
        return true
    }

    override fun registerListener(listener: LoggerXCallback?) {
        this.callback = listener
    }
}