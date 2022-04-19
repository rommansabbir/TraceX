package com.rommansabbir.tracex.processkiller

import kotlin.system.exitProcess

object ProcessKiller {
    /**
     * Terminates the currently running process as normal termination.
     *
     * @return [Nothing].
     */
    fun killProcess(): Nothing = exitProcess(0)
}