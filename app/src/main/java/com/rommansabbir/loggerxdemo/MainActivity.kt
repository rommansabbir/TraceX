package com.rommansabbir.loggerxdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rommansabbir.loggerx.registerForLoggerX

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*registerForLoggerX(
            object : LoggerXCallback {
                override fun onEvent(deviceInfo: DeviceInfo, thread: Thread, throwable: Throwable) {

                }
            }
        )*/
        registerForLoggerX { deviceInfo, thread, throwable ->
            Toast.makeText(this@MainActivity, throwable.message, Toast.LENGTH_SHORT).show()
        }
        Thread.sleep(3000)
        throw RuntimeException("Test")
    }
}