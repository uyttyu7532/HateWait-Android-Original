package com.example.hatewait

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.hatewait.login.MainActivity

class Splash : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val TIME_OUT: Long = 2000

        Handler().postDelayed(Runnable {
            kotlin.run {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, TIME_OUT)
    }
}