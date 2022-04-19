package com.rommansabbir.tracex.extensions

import java.util.*

/*Prefix for searching logs from cache or to write a new log to the cache directory*/
internal const val traceXPrefix = "TraceX_Log_"

/*Generate a new caching key*/
internal val getCachingKey: String = "${traceXPrefix}${Calendar.getInstance().time.time}"