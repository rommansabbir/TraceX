package com.rommansabbir.tracex

import android.app.Activity
import com.rommansabbir.storex.StoreX
import com.rommansabbir.tracex.config.TraceXConfig
import com.rommansabbir.tracex.exception.TraceXNotInitializedException
import com.rommansabbir.tracex.model.TraceXCrashLog

/**
 * [TraceX] is designed to monitor all Uncaught Exception occurs in app lifecycle by default.
 * Also, client can disable auto monitoring Uncaught Exception by following the [TraceXConfig]
 * and manually register an [Activity] for monitoring by calling [TraceX.registerActivity] API.
 *
 * [TraceX] will automatically write a log to the app cache directory if the Uncaught Exception
 * is an instance of [RuntimeException] based on Config. Client can also write a new log the the
 * app cache directory by calling [TraceX.writeANewLog] API.
 *
 * Also, client can get all logs written by [TraceX] or client itself by
 * calling [TraceX.writeANewLog] API.
 *
 * Client can also remove a list of managed or unmanaged logs from the cache directory that is
 * written by [TraceX]. Or, simply remove all logs from cache directory written by [TraceX].
 *
 * **Motto of this library:**
 *
 * As a developer we don't know on which device or on which constraint system
 * will throw [Exception] or [RuntimeException] if the application is in PRODUCTION. If any Uncaught
 * Exception occurs during app lifecycle we get to know about it via others Logging library.
 *
 * But, as a developer you might want to know navigate the user to a specific page (eg. home page)
 * when an fatal exception occurs which eventually kill the application process in the device. Before
 * that page navigation, we can write a log to the app cache directory by following the current
 * Device Info, Current Thread, Throwable that occurred and a JSON object as additional info.
 * Or simply, we can write our own log to the cache directory at our own.
 *
 * So that, we can get the list written logs from the cache directory when user run the
 * application again, we can process the logs, like SEND IT TO THE REMOTE SERVER for bug fixing,
 * analytics or simply ignore or remove the log from the cache directory.
 *
 * **NOTE:** Writing or Reading logs from cache directory follows **Encryption/Decryption** process
 * by using [StoreX].
 */
interface TraceX {
    /**
     * Initialize [TraceX] with required [TraceXConfig].
     *
     * Note: [TraceX]'s all APIs will only work when [TraceX] is initialized properly before accessing
     * it's APIs, otherwise it will throw [TraceXNotInitializedException].
     *
     * @return [Boolean] Is successful or not.
     */
    fun init(config: TraceXConfig): Boolean

    /**
     * Register for a listener, [TraceXCallback] to get notified
     * about Uncaught error event occurs if Initialized. Else, throw [TraceXNotInitializedException].
     *
     * @param listener [TraceXCallback] can be nullable if client want to detach the listener.
     *
     * @throws [TraceXNotInitializedException] If [TraceX] is not initialized.
     */
    @Throws(TraceXNotInitializedException::class)
    fun registerListener(listener: TraceXCallback?)

    /**
     * Manually register an [Activity] for Uncaught Exception Handling,
     * if [TraceX] is Initialized. Else, throw [TraceXNotInitializedException].
     *
     * @param activity [Activity] can be nullable if client want to detach the activity.
     *
     * @throws [TraceXNotInitializedException] If [TraceX] is not initialized.
     */
    @Throws(TraceXNotInitializedException::class)
    fun registerActivity(activity: Activity?): Boolean

    /**
     * Report a new log to the [TraceX] system. [TraceX] will write the [Throwable] as readable string
     * like in the IDE console. Client can also pass an [additionalInfo] object, a json object
     * since [TraceX] allows only [String] as [additionalInfo].An instance of [TraceXCrashLog] is written
     * to the cache directory (**Encrypted**). Client can check for all [TraceXCrashLog] written by
     * [TraceX] or client itself by calling [getRecentCrashLogs] API.
     *
     * Check if [TraceX] is Initialized.
     * Else, throw [TraceXNotInitializedException].
     *
     * @param throwable [Throwable] object to be written in the log.
     * @param additionalInfo JSON object to be stored in the log as additional info.
     *
     * @see [getRecentCrashLogs] if client want to get the list of written logs.
     *
     * @throws [TraceXNotInitializedException] If [TraceX] is not initialized.
     */
    @Throws(TraceXNotInitializedException::class)
    fun writeANewLog(throwable: Throwable, additionalInfo: String = ""): Boolean

    /**
     * To get all [TraceXCrashLog] written by [TraceX] or client itself by calling [writeANewLog] API.
     *
     * Check if [TraceX] is Initialized.
     * Else, throw [TraceXNotInitializedException].
     *
     * @see [writeANewLog] if client want to write a new log.
     *
     * @throws [TraceXNotInitializedException] If [TraceX] is not initialized.
     */
    @Throws(TraceXNotInitializedException::class)
    fun getRecentCrashLogs(): MutableList<TraceXCrashLog>


    /**
     * To remove a list of provided [TraceXCrashLog]. [TraceX] will remove
     * all cached logs that exactly matched to the provided [list].
     *
     * Check if [TraceX] is Initialized.
     * Else, throw [TraceXNotInitializedException].
     *
     * @see [clearAllLogs] if client want to remove all logs.
     *
     * @throws [TraceXNotInitializedException] If [TraceX] is not initialized.
     */
    @Throws(TraceXNotInitializedException::class)
    fun clearCrashLogs(list: MutableList<TraceXCrashLog>)

    /**
     * Remove all cached logs written by [TraceX] itself.
     *
     * Check if [TraceX] is Initialized.
     * Else, throw [TraceXNotInitializedException].
     *
     * @see [clearCrashLogs] if client want to remove some specific list of logs.
     *
     * @throws [TraceXNotInitializedException] If [TraceX] is not initialized.
     */
    @Throws(TraceXNotInitializedException::class)
    fun clearAllLogs(): Boolean
}


