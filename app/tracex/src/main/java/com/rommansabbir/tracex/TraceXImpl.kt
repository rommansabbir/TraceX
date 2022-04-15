package com.rommansabbir.tracex

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.os.Bundle
import com.rommansabbir.storex.StoreXConfig
import com.rommansabbir.storex.StoreXCore
import com.rommansabbir.tracex.model.DeviceInfo
import com.rommansabbir.tracex.model.TraceXCrashLog
import java.lang.ref.WeakReference


internal class TraceXImpl : TraceX {
    private var config: TraceXConfig? = null
    private var callback: TraceXCallback? = null
    private val storeXConfig = StoreXConfig("LoggerX", "LoggerX", true)

    @SuppressLint("HardwareIds")
    private fun getSystemDetail(contentResolver: ContentResolver): DeviceInfo =
        DeviceInfo.Builder.build(contentResolver)


    private val thread = Thread.UncaughtExceptionHandler { t, e ->
        activity.get()?.let {
            if (e is RuntimeException) {
                if (writeLogToCacheDir(e, "")) notifyClient(it, t, e)
            } else {
                notifyClient(it, t, e)
            }
        }
    }

    private fun writeLogToCacheDir(e: Throwable, additionalInfo: String): Boolean {
        if (config?.application == null) {
            return false
        }
        return try {
            val key = getCachingKey
            StoreXCore.instance(storeXConfig).put(
                key,
                TraceXCrashLog(
                    key,
                    getSystemDetail(config!!.application.contentResolver),
                    e.makeReadable(),
                    additionalInfo
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun notifyClient(it: Activity, t: Thread, e: Throwable) {
        callback?.onEvent(
            getSystemDetail(
                it.contentResolver
            ), t,
            e,
            ProcessKiller
        )
    }

    private var activity: WeakReference<Activity> = WeakReference(null)


    // Callback for activity lifecycle for this specific application
    private val activityCallback = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (config?.autoRegisterForEachActivity == true) {
                /*Register Thread.setDefaultUncaughtExceptionHandler for every activity by default*/
                this@TraceXImpl.activity = WeakReference(activity)
                unregisterOrUnregister(true)
            }
        }

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            unregisterOrUnregister(false)
            this@TraceXImpl.activity = WeakReference(null)
        }

    }

    private fun unregisterOrUnregister(isRegister: Boolean) {
        activity.get()?.apply {
            Thread.setDefaultUncaughtExceptionHandler(if (isRegister) thread else null)
        }
    }

    override fun init(config: TraceXConfig): Boolean {
        this.config = config
        this.config!!.application.registerActivityLifecycleCallbacks(activityCallback)
        StoreXCore.init(config.application, mutableListOf(storeXConfig))
        StoreXCore.setEncryptionKey(BuildConfig.LOGGER_X_LOGGING_ENCRYPTION_KEY)
        return true
    }

    override fun registerListener(listener: TraceXCallback?) {
        this.callback = listener
    }

    override fun registerActivity(activity: Activity?): Boolean {
        checkInit()
        if (config?.autoRegisterForEachActivity == false) {
            this.activity = WeakReference(activity)
            unregisterOrUnregister(true)
            return true
        }
        return false
    }

    override fun reportLog(throwable: Throwable, additionalInfo: String): Boolean {
        checkInit()
        return writeLogToCacheDir(throwable, additionalInfo)
    }


    override fun getRecentCrashLogs(): MutableList<TraceXCrashLog> {
        checkInit()
        val listOfCrashLogs: MutableList<TraceXCrashLog> = mutableListOf()
        try {
            config?.application?.cacheDir?.listFiles()?.forEach {
                it?.let {
                    if (it.name.contains(traceXPrefix)) {
                        try {
                            listOfCrashLogs.add(
                                StoreXCore.instance(storeXConfig)
                                    .get(it.name, TraceXCrashLog::class.java)
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return listOfCrashLogs
    }

    private fun checkInit() {
        if (config?.application == null) {
            initException()
        }
    }

    override fun clearCrashLogs(list: MutableList<TraceXCrashLog>) {
        checkInit()
        list.forEach { model ->
            config?.application?.cacheDir?.listFiles()?.find { it.name == model.key }?.let {
                try {
                    it.delete()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun clearAllLogs(): Boolean {
        checkInit()
        config?.application?.cacheDir?.listFiles()?.forEach {
            try {
                if (it.name.contains(traceXPrefix)) {
                    it.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return true
    }
}