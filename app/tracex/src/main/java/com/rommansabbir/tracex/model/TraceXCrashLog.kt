package com.rommansabbir.tracex.model

import com.rommansabbir.storex.StoreAbleObject

data class TraceXCrashLog(
    val key: String,
    val deviceInfo: DeviceInfo,
    val stackTrace: String,
    /*JSON object as additional info*/
    val additionalInfo: String
) : StoreAbleObject()