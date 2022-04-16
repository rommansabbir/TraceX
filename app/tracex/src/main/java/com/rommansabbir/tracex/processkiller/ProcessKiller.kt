package com.rommansabbir.tracex.processkiller

import kotlin.system.exitProcess

object ProcessKiller {
    /**
     * Terminates the currently running process as normal termination.
     */
    fun killProcess() {
        exitProcess(0)
    }
}