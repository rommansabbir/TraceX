package com.rommansabbir.tracex.exception

/**
 * An [Exception] to identity that TraceX is not initialized properly before accessing it's functionality.
 */
class TraceXNotInitializedException constructor(override val message: String = "TraceX not initialized.") : Exception(message)