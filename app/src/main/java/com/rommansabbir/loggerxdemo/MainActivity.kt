package com.rommansabbir.loggerxdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.rommansabbir.loggerx.registerForLoggerX
import kotlin.system.exitProcess

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
        registerForLoggerX { _, _, throwable ->
            if (throwable is RuntimeException) {
                startSecond(throwable)
            } else {
                Toast.makeText(this@MainActivity, throwable.message, Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<MaterialButton>(R.id.button_main).setOnClickListener {
            throw RuntimeException("Test")
        }
    }

    private fun startSecond(throwable: Throwable) {
        SecondActivity.Factory.startActivity(this, throwable)
        exitProcess(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Destroy")
    }
}

