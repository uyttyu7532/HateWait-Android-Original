package com.example.hatewait.storeinfo

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.hatewait.R
import com.example.hatewait.socket.StoreRegisterAsyncTask

import kotlinx.android.synthetic.main.activity_store_register4.*

class StoreSignUp4 : AppCompatActivity(),
    AutoCallNumberChangeDialog.DialogListener {
    private val REQUEST_CODE_BUSINESS_TIME = 2000

    override fun applyPickedNumber(autoCallNumber: String) {
        auto_call_number_textView.text = autoCallNumber
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_register4)
        setSupportActionBar(register_toolbar4)
        supportActionBar?.apply {
            //            Set this to true if selecting "home" returns up by a single level in your UI rather than back to the top level or front page.
            setDisplayHomeAsUpEnabled(true)
//            you should also call setHomeActionContentDescription() to provide a correct description of the action for accessibility support.
            setHomeAsUpIndicator(R.drawable.back_icon)
            setHomeActionContentDescription("가게 주소 & 전화번호 & 수용인원 설정")
            setDisplayShowTitleEnabled(false)
        }

        auto_call_number_textView.setOnClickListener {
            AutoCallNumberChangeDialog()
                .show(supportFragmentManager, "STORE_AUTO_CALL")
        }
        store_business_hours_textView.setOnClickListener {
            val intent = Intent(this@StoreSignUp4, BusinessHourPick::class.java)
            startActivityForResult(intent, 2000)
        }

        button_finish.setOnClickListener {
            val newStoreInfo = mapOf(
                Pair("ID", intent.getStringExtra("STORE_ID")),
                Pair("PASSWORD", intent.getStringExtra("STORE_PASSWORD")),
                Pair("NAME", intent.getStringExtra("STORE_NAME")),
                Pair("DESCRIPTION", intent.getStringExtra("STORE_DESCRIPTION")),
                Pair("ADDRESS", intent.getStringExtra("STORE_ADDRESS")),
                Pair("PHONE", intent.getStringExtra("STORE_PHONE")),
                Pair("CAPACITY", intent.getStringExtra("STORE_CAPACITY")),
                Pair("AUTO_CALL", auto_call_number_textView.text.toString()),
                Pair("BUSINESS_HOURS", store_business_hours_textView.text.toString())
            )
            StoreRegisterAsyncTask(this@StoreSignUp4).execute(newStoreInfo)
            Toast.makeText(this, "성공!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_BUSINESS_TIME) {
            if (resultCode == 200) {
                store_business_hours_textView.text = data?.getStringExtra("UPDATED_BUSINESS_TIME")
//                영업시간을 성공적으로 설정하면 최종 '가입하기' 버튼이 활성화됨
                button_finish.isEnabled = true
            }
            if (resultCode == 400) {
//                nothing to do (failed to update business Time)
            }
        }
    }

}
