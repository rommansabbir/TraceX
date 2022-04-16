package com.rommansabbir.tracex

import com.rommansabbir.tracex.model.DeviceInfo
import com.rommansabbir.tracex.processkiller.ProcessKiller

/**
 * To notify clients regarding an UnCaught error event
 * along with [DeviceInfo],current [Thread], [Throwable] and [ProcessKiller].
 *
 * Note: Client must call [ProcessKiller.killProcess] after consuming the event
 * to notify system that it's an normal termination of current process.
 */
interface TraceXCallback {
    /**
     * Get notified about an UnCaught error event.
     *
     * @param deviceInfo Current [DeviceInfo].
     * @param thread Current [Thread].
     * @param throwable [Throwable] that just occurred.
     * @param processKiller [ProcessKiller] to kill process after consuming the [Throwable].
     */
    fun onEvent(
        deviceInfo: DeviceInfo,
        thread: Thread,
        throwable: Throwable,
        processKiller: ProcessKiller
    )
}