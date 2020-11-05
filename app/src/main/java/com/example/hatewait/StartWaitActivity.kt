package com.example.hatewait

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hatewait.login.LoginRegisterViewPagerActivity
import kotlinx.android.synthetic.main.activity_start_wait.*
import org.jetbrains.anko.startActivity

class StartWaitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_wait)

        start_waiting_button.setOnClickListener{
            startActivity<LoginRegisterViewPagerActivity>()
        }
    }
}