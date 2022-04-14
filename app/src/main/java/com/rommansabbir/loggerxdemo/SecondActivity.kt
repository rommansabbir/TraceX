package com.rommansabbir.loggerxdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rommansabbir.loggerx.registerForLoggerX

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
        registerForLoggerX { _, _, throwable ->
            if (throwable is RuntimeException) {
                println("")
            } else {
                Toast.makeText(this@SecondActivity,"Occurred Exception: "+ throwable.message, Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<TextView>(R.id.textView_second).text =
            "Occurred Exception: "+(intent.getSerializableExtra("exception") as Throwable).message.toString()
    }
}