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
import com.rommansabbir.tracex.exception.TraceXNotInitializedException
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
        StoreXCore.setEncryptionKey(BuildConfig.STOREX_X_LOGGING_ENCRYPTION_KEY)
    }

    /**
     * Check if [TraceX] is initialized properly or not.
     * If not throw [TraceXNotInitializedException].
     */
    protected fun checkInitialization() {
        if (config?.application == null) {
            throwInitException()
        }
    }

    /**
     * Callback to get notified on Uncaught Exceptions.
     */
    private val thread = Thread.UncaughtExceptionHandler { t, e ->
        activity.get()?.let {
            /*Write a new log to the cache directory if exception is an RuntimeException
            and config is set to true*/
            if (e is RuntimeException && config?.autoLogRuntimeExceptions == true) {
                if (writeANewLog(e, "")) notifyClient(it, t, e)
            } else {
                notifyClient(it, t, e)
            }
        }
    }

    /**
     * Register or Unregister for Uncaught Exceptions.
     *
     * @param isRegister determine to subscribe or to remove.
     */
    protected fun unregisterOrUnregisterUncaughtExceptionHandler(isRegister: Boolean) {
        activity.get()?.apply {
            Thread.setDefaultUncaughtExceptionHandler(if (isRegister) thread else null)
        }
    }

    /**
     * Build a new instance of [DeviceInfo] from current device system.
     *
     * @param contentResolver [ContentResolver] reference.
     *
     * @return [DeviceInfo]
     */
    private fun getSystemDetail(contentResolver: ContentResolver): DeviceInfo =
        DeviceInfo.Builder.build(contentResolver)

    /**
     * Write a new log to the cache directory using [StoreXCore.instance].
     *
     * @param e [Throwable] to be simplified and to be written in the log.
     * @param additionalInfo JSON object as additional info.
     *
     * @return [Boolean].
     */
    protected fun writeLogToCacheDir(e: Throwable, additionalInfo: String): Boolean {
        /*If application is null return false, instead of throwing exception as we already
        *checked that before accessing public APIs*/
        if (config?.application == null) {
            return false
        }
        return try {
            /*Get a new caching key*/
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

    /**
     * Notify clients regarding error event.
     *
     * @param it [Activity] reference.
     * @param t Current [Thread].
     * @param e [Throwable] to be passed.
     */
    private fun notifyClient(it: Activity, t: Thread, e: Throwable) {
        callback?.onEvent(
            getSystemDetail(
                it.contentResolver
            ), t,
            e,
            ProcessKiller
        )
    }

    /**
     * Clear all given logs written by [TraceX].
     *
     * Note: It's a CPU Intensive process, execute the operation with Coroutine/RxJava.
     *
     * @param list List to be removed from cache.
     */
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

    /**
     * Clear all logs written by [TraceX].
     *
     * Note: It's a CPU Intensive process, execute the operation with Coroutine/RxJava.
     */
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

    /**
     * Get all logs written by [TraceX].
     *
     * Note: It's a CPU Intensive process, execute the operation with Coroutine/RxJava.
     *
     * @return [MutableList]<[TraceXCrashLog]>.
     */
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

    /**
     * Register given [Activity] (which can be nullable) for Uncaught Exceptions.
     *
     * @param activity [Activity] to subscribe or remove handing exceptions.
     *
     * @return [Boolean].
     */
    protected fun registerActivityBase(activity: Activity?): Boolean {
        if (config?.autoRegisterForEachActivity == false) {
            this.activity = WeakReference(activity)
            unregisterOrUnregisterUncaughtExceptionHandler(true)
            return true
        }
        return false
    }

}