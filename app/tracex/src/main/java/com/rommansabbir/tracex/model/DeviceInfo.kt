package com.rommansabbir.tracex.model

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.os.Build
import android.provider.Settings

@SuppressLint("HardwareIds")
class DeviceInfo private constructor(
    val brand: String,
    val deviceId: String,
    val model: String,
    val id: String,
    val sdk: String,
    val manufacturer: String,
    val user: String,
    val type: String,
    val base: String,
    val incremental: String,
    val board: String,
    val host: String,
    val fingerPrint: String,
    val versionCode: String
) {
    object Builder {
        fun build(contentResolver: ContentResolver? = null): DeviceInfo {
            return DeviceInfo(
                Build.BRAND,
                try {
                    if (contentResolver == null) {
                        ""
                    } else {
                        Settings.Secure.getString(
                            contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                    }
                } catch (e: Exception) {
                    ""
                },
                Build.MODEL,
                Build.ID,
                Build.VERSION.SDK_INT.toString(),
                Build.MANUFACTURER,
                Build.USER,
                Build.TYPE,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Build.VERSION.BASE_OS else "",
                Build.VERSION.INCREMENTAL,
                Build.BOARD,
                Build.HOST,
                Build.FINGERPRINT,
                Build.HARDWARE
            )
        }
    }
}