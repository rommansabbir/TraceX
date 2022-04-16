package com.rommansabbir.tracexdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.rommansabbir.tracex.processkiller.ProcessKiller
import com.rommansabbir.tracex.provider.TraceXProvider
import com.rommansabbir.tracex.extensions.registerForTraceX

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TraceXProvider.INSTANCE.registerActivity(this)
        /*registerForLoggerX(
            object : LoggerXCallback {
                override fun onEvent(deviceInfo: DeviceInfo, thread: Thread, throwable: Throwable) {

                }
            }
        )*/
        registerForTraceX { _, _, throwable, p ->
            TraceXProvider.INSTANCE.writeANewLog(throwable, "Test Exception.")
            Toast.makeText(this@MainActivity, throwable.message, Toast.LENGTH_SHORT).show()
            p.killProcess()
/*            if (throwable is RuntimeException) {
                startSecond(throwable)
            } else {
                TraceXProvider.INSTANCE.reportLog(throwable, "Put your JSON object here.")
                Toast.makeText(this@MainActivity, throwable.message, Toast.LENGTH_SHORT).show()
            }*/
        }
        findViewById<MaterialButton>(R.id.button_main).setOnClickListener {
            /*LoggerXProvider.INSTANCE.clearAllLogs(this)*/
            throw Exception("Test")
        }
    }

    private fun startSecond(throwable: Throwable) {
        SecondActivity.Factory.startActivity(this, throwable)
        ProcessKiller.killProcess()
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Destroy")
    }
}

