package com.rommansabbir.loggerx

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.os.Bundle
import com.rommansabbir.loggerx.model.DeviceInfo
import java.lang.ref.WeakReference


internal class LoggerXImpl : LoggerX {
    private var config: LoggerXConfig? = null
    private var callback: LoggerXCallback? = null

    @SuppressLint("HardwareIds")
    private fun getSystemDetail(contentResolver: ContentResolver): DeviceInfo =
        DeviceInfo.Builder.build(contentResolver)


    private val thread = Thread.UncaughtExceptionHandler { t, e ->
        activity.get()?.let {
            callback?.onEvent(
                getSystemDetail(
                    it.contentResolver
                ), t,
                e
            )
        }
    }

    private var activity: WeakReference<Activity> = WeakReference(null)


    // Callback for activity lifecycle for this specific application
    private val activityCallback = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            /*Register Thread.setDefaultUncaughtExceptionHandler for every activity by default*/
            this@LoggerXImpl.activity = WeakReference(activity)
            activity.apply {
                Thread.setDefaultUncaughtExceptionHandler(thread)
            }
        }

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            unregister(activity)
            this@LoggerXImpl.activity = WeakReference(null)
        }

    }

    private fun unregister(activity: Activity) {
        activity.apply {
            Thread.setDefaultUncaughtExceptionHandler(null)
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