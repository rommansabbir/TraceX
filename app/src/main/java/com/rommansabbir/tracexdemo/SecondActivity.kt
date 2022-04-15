package com.rommansabbir.tracexdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rommansabbir.tracex.registerForTraceX
import com.rommansabbir.tracex.makeReadable

class SecondActivity : AppCompatActivity() {
    object Factory {
        fun startActivity(activity: MainActivity, exception: Throwable) {
            activity.startActivity(
                Intent(
                    activity,
                    SecondActivity::class.java
                ).putExtra("exception", exception)
            )
            activity.finish()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        registerForTraceX { _, _, throwable,p ->
            if (throwable is RuntimeException) {
                println("")
            } else {
                Toast.makeText(
                    this@SecondActivity,
                    "Occurred Exception: " + throwable.makeReadable(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val e = (intent.getSerializableExtra("exception") as Throwable)
        findViewById<TextView>(R.id.textView_second).text =
            "Occurred Exception: ${e.makeReadable()}"
    }
}