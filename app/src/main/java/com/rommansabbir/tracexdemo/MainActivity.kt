package com.rommansabbir.tracexdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.rommansabbir.tracex.extensions.registerForTraceX
import com.rommansabbir.tracex.processkiller.ProcessKiller
import com.rommansabbir.tracex.provider.TraceXProvider

@SuppressLint("SetTextI18n")

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*Register for Uncaught Error event*/
        registerForTraceX { _, _, throwable, p ->
            /*If RuntimeException, go to another activity*/
            if (throwable is DemoException) {
                startSecond(throwable)
            }
            Toast.makeText(this@MainActivity, throwable.message, Toast.LENGTH_SHORT).show()
            /*Kill the process normally*/
            p.killProcess()
        }
        findViewById<MaterialButton>(R.id.button_main).setOnClickListener {
            throw RuntimeException("Test")
        }
        findViewById<MaterialButton>(R.id.btn_go_to_next).setOnClickListener {
            throw DemoException()
        }
        getAllLogs()
    }

    private fun getAllLogs() {
        val logs = TraceXProvider.INSTANCE.getRecentCrashLogs()
        findViewById<TextView>(R.id.tv_logs_counter).text =
            "${getString(R.string.total_logs_found)} ${logs.size}"
        if (logs.size > 0) {
            val additionalInfo: String = logs[0].additionalInfo
            val stacktrace = logs[0].stackTrace
            findViewById<TextView>(R.id.tv_recent_log).text =
                "${getString(R.string.most_recent_log)}\nAdditional Info: ${additionalInfo}\n${stacktrace}"
        }
    }

    private fun startSecond(throwable: Throwable) {
        SecondActivity.Factory.startActivity(this, throwable)
        /*Kill the process normally*/
        ProcessKiller.killProcess()
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Destroy")
    }
}

