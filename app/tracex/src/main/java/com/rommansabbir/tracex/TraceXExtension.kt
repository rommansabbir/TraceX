package com.rommansabbir.tracex

import androidx.activity.ComponentActivity
import com.rommansabbir.tracex.exception.TraceXNotInitializedException
import com.rommansabbir.tracex.model.DeviceInfo
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*

fun ComponentActivity.registerForTraceX(callback: TraceXCallback?) {
    TraceXProvider.INSTANCE.registerListener(callback)
}

fun ComponentActivity.registerForTraceX(callback: (deviceInfo: DeviceInfo, thread: Thread, throwable: Throwable, processKiller: ProcessKiller) -> Unit = { _, _, _, _ -> }) {
    TraceXProvider.INSTANCE.registerListener(
        object : TraceXCallback {
            override fun onEvent(
                deviceInfo: DeviceInfo,
                thread: Thread,
                throwable: Throwable,
                processKiller: ProcessKiller
            ) {
                callback.invoke(deviceInfo, thread, throwable, processKiller)
            }
        }
    )
}

/**
 * Extension function to return stacktrace as readable string.
 *
 * @return [String]
 */
fun Throwable.makeReadable(): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    this.printStackTrace(pw)
    return sw.toString()
}

internal const val traceXPrefix = "TraceX_Log_"
internal val getCachingKey: String = "${traceXPrefix}${Calendar.getInstance().time.time}"

fun initException() {
    throw TraceXNotInitializedException()
}


