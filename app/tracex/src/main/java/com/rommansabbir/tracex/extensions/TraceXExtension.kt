package com.rommansabbir.tracex.extensions

import androidx.activity.ComponentActivity
import com.rommansabbir.tracex.processkiller.ProcessKiller
import com.rommansabbir.tracex.TraceXCallback
import com.rommansabbir.tracex.provider.TraceXProvider
import com.rommansabbir.tracex.model.DeviceInfo

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




