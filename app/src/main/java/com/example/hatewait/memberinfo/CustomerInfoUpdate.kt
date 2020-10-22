package com.example.hatewait.memberinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hatewait.R
import com.example.hatewait.storeinfo.ChangePasswordActivity1
import com.example.hatewait.storeinfo.StorePhoneNumberChangeDialog
import kotlinx.android.synthetic.main.activity_customer_info_update.*
import org.jetbrains.anko.startActivity

class CustomerInfoUpdate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_info_update)

        setting_customer_phone_num.setOnClickListener {
            StorePhoneNumberChangeDialog()
                .show(supportFragmentManager, "STORE_PHONE_CHANGE")
        }
        setting_customer_password.setOnClickListener{
            startActivity<ChangePasswordActivity1>()
        }
    }
}