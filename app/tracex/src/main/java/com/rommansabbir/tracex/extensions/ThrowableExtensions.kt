package com.rommansabbir.tracex.extensions

import com.rommansabbir.tracex.exception.TraceXNotInitializedException
import java.io.PrintWriter
import java.io.StringWriter


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

/**
 * Throw a new exception [TraceXNotInitializedException].
 */
internal fun throwInitException() {
    throw TraceXNotInitializedException()
}