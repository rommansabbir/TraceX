package com.rommansabbir.tracex.exception

class TraceXNotInitializedException constructor(override val message: String = "TraceX not initialized.") :
    Exception(message) {
}