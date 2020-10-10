package com.example.hatewait.customerinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_customer_info_update.*
import org.jetbrains.anko.startActivity

class CustomerInfoUpdate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_info_update)

        setting_password_customer.setOnClickListener{
            startActivity<SettingPassword>()
        }
    }
}