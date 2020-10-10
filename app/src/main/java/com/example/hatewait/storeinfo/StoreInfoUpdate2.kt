package com.example.hatewait.storeinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_store_info_update.*
import kotlinx.android.synthetic.main.activity_store_info_update2.*

class StoreInfoUpdate2 : AppCompatActivity(){
    private val REQUEST_CODE_BUSINESS_TIME = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_info_update2)
        


        setting_time.setOnClickListener {
            val intent = Intent(this@StoreInfoUpdate2, BusinessHourPick::class.java)
            startActivityForResult(intent, 2000)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_BUSINESS_TIME) {
            if (resultCode == 200) {
                store_business_hours_text.text = data?.getStringExtra("UPDATED_BUSINESS_TIME")
            }
            if (resultCode == 400) {
//                nothing to do (failed to update business Time)
            }
        }
    }
}