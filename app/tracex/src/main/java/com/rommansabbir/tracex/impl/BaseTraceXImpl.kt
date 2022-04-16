package com.rommansabbir.tracex.impl

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.os.Bundle
import com.rommansabbir.storex.StoreXConfig
import com.rommansabbir.storex.StoreXCore
import com.rommansabbir.tracex.BuildConfig
import com.rommansabbir.tracex.TraceX
import com.rommansabbir.tracex.TraceXCallback
import com.rommansabbir.tracex.config.TraceXConfig
import com.rommansabbir.tracex.extensions.getCachingKey
import com.rommansabbir.tracex.extensions.makeReadable
import com.rommansabbir.tracex.extensions.throwInitException
import com.rommansabbir.tracex.extensions.traceXPrefix
import com.rommansabbir.tracex.model.DeviceInfo
import com.rommansabbir.tracex.model.TraceXCrashLog
import com.rommansabbir.tracex.processkiller.ProcessKiller
import java.lang.ref.WeakReference

@SuppressLint("HardwareIds")
internal abstract class BaseTraceXImpl : TraceX {
    protected var config: TraceXConfig? = null
    protected var callback: TraceXCallback? = null
    private val storeXConfig = StoreXConfig("LoggerX", "LoggerX", true)
    protected var activity: WeakReference<Activity> = WeakReference(null)

    // Callback for activity lifecycle for this specific application
    private val activityCallback = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (config?.autoRegisterForEachActivity == true) {
                /**
                 * Register [Thread.setDefaultUncaughtExceptionHandler] for every activity by default
                 */
                this@BaseTraceXImpl.activity = WeakReference(activity)
                unregisterOrUnregisterUncaughtExceptionHandler(true)
            }
        }

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            /**
             * When any activity destroy happens,
             * remove [Activity] reference and [unregisterOrUnregisterUncaughtExceptionHandler]
             */
            unregisterOrUnregisterUncaughtExceptionHandler(false)
            this@BaseTraceXImpl.activity = WeakReference(null)
        }

    }

    fun baseInit(config: TraceXConfig): Boolean {
        this.config = config
        this.config!!.application.registerActivityLifecycleCallbacks(activityCallback)
        initStoreX(config)
        return true
    }

    private fun initStoreX(config: TraceXConfig) {
        StoreXCore.init(config.application, mutableListOf(storeXConfig))
        StoreXCore.setEncryptionKey(BuildConfig.LOGGER_X_LOGGING_ENCRYPTION_KEY)
    }

    protected fun checkInitialization() {
        if (config?.application == null) {
            throwInitException()
        }
    }


    private val thread = Thread.UncaughtExceptionHandler { t, e ->
        activity.get()?.let {
            if (e is RuntimeException) {
                if (writeANewLog(e, "")) notifyClient(it, t, e)
            } else {
                notifyClient(it, t, e)
            }
        }
    }

    protected fun unregisterOrUnregisterUncaughtExceptionHandler(isRegister: Boolean) {
        activity.get()?.apply {
            Thread.setDefaultUncaughtExceptionHandler(if (isRegister) thread else null)
        }
    }

    private fun getSystemDetail(contentResolver: ContentResolver): DeviceInfo =
        DeviceInfo.Builder.build(contentResolver)

    protected fun writeLogToCacheDir(e: Throwable, additionalInfo: String): Boolean {
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

    protected fun clearCrashLogsBase(list: MutableList<TraceXCrashLog>) {
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

    protected fun clearAllLogsBase() {
        config?.application?.cacheDir?.listFiles()?.forEach {
            try {
                if (it.name.contains(traceXPrefix)) {
                    it.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    protected fun getRecentCrashLogsBase(): MutableList<TraceXCrashLog> {
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

    protected fun registerActivityBase(activity: Activity?): Boolean {
        if (config?.autoRegisterForEachActivity == false) {
            this.activity = WeakReference(activity)
            unregisterOrUnregisterUncaughtExceptionHandler(true)
            return true
        }
        return false
    }

}