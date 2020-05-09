package com.example.hatewait

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler

class Splash : Activity(){

    val TIME_OUT : Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable {
            kotlin.run {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, TIME_OUT)
    }
}