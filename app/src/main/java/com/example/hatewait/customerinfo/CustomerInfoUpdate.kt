package com.example.hatewait.customerinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hatewait.R
import com.example.hatewait.storeinfo.ChangePasswordActivity1
import com.example.hatewait.storeinfo.StorePhoneNumberChangeDialog
import kotlinx.android.synthetic.main.activity_customer_info_update.*
import kotlinx.android.synthetic.main.activity_store_info_update2.*
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