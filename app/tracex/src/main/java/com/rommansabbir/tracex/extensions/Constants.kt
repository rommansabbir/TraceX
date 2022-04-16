package com.rommansabbir.tracex.extensions

import java.util.*

internal const val traceXPrefix = "TraceX_Log_"
internal val getCachingKey: String = "${traceXPrefix}${Calendar.getInstance().time.time}"