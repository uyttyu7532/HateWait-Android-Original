package com.example.hatewait.storeinfo

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import com.example.hatewait.R
import com.example.hatewait.socket.StoreRegisterAsyncTask
import kotlinx.android.synthetic.main.activity_store_register3.*

import kotlinx.android.synthetic.main.activity_store_register4.button_finish
import kotlinx.android.synthetic.main.activity_store_register4.store_business_hours_textView

class StoreSignUp3 : AppCompatActivity(){

    private val storeCapacityRegex = Regex("[^0](\\d{0,3})")

    fun verifyCapacity(capacityNumber: String): Boolean = storeCapacityRegex.matches(capacityNumber)
    private val REQUEST_CODE_BUSINESS_TIME = 2000
    private var isBusinessHours = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_register3)
        setSupportActionBar(register_toolbar3)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setHomeActionContentDescription("가게 주소 & 전화번호 & 수용인원 설정")
            setDisplayShowTitleEnabled(false)
        }

        store_business_hours_textView.setOnClickListener {
            val intent = Intent(this@StoreSignUp3, BusinessHourPick::class.java)
            startActivityForResult(intent, 2000)
        }

        button_finish.setOnClickListener {
            val newStoreInfo = mapOf(
                Pair("ID", intent.getStringExtra("STORE_ID")),
                Pair("PASSWORD", intent.getStringExtra("STORE_PASSWORD")),
                Pair("NAME", intent.getStringExtra("STORE_NAME")),
                Pair("PHONE", intent.getStringExtra("STORE_PHONE")),
                Pair("ADDRESS", intent.getStringExtra("STORE_ADDRESS")),
                Pair("BUSINESS_HOURS", store_business_hours_textView.text.toString()),
                Pair("CAPACITY", intent.getStringExtra("STORE_CAPACITY")),
                Pair("DESCRIPTION", intent.getStringExtra("STORE_DESCRIPTION"))
            )
            StoreRegisterAsyncTask(this@StoreSignUp3).execute(newStoreInfo)
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
                isBusinessHours = true
                button_finish.isEnabled =
                    store_capacity_layout.error == null
                            && store_info_description_layout.error == null
                            && !store_info_description_editText.text.isNullOrBlank()
                            && !store_capacity_editText.text.isNullOrBlank()
                            && !store_capacity_editText.text.isNullOrBlank()
                            && isBusinessHours
            }
            if (resultCode == 400) {
//                nothing to do (failed to update business Time)
            }
        }
    }

    private fun addTextChangeListener() {

        // 수용 인원
        store_capacity_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(capacityNumber: Editable?) {
                if (!verifyCapacity(capacityNumber.toString())) {
                    store_capacity_layout.error = "9999명까지 입력 가능합니다."
                    button_finish.isEnabled = false
                } else {
                    store_capacity_layout.error = null
                    store_capacity_layout.hint = null
                }
                button_finish.isEnabled =
                    store_capacity_layout.error == null
                            && store_info_description_layout.error == null
                            && !store_info_description_editText.text.isNullOrBlank()
                            && !store_capacity_editText.text.isNullOrBlank()
                            && isBusinessHours
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // 가게 소개
        store_info_description_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(capacityNumber: Editable?) {
                if (!verifyCapacity(capacityNumber.toString())) {
                    store_capacity_layout.error = "9999명까지 입력 가능합니다."
                    button_finish.isEnabled = false
                } else {
                    store_capacity_layout.error = null
                    store_capacity_layout.hint = null
                }
                button_finish.isEnabled =
                    store_capacity_layout.error == null
                            && store_info_description_layout.error == null
                            && !store_info_description_editText.text.isNullOrBlank()
                            && !store_capacity_editText.text.isNullOrBlank()
                            && !store_capacity_editText.text.isNullOrBlank()
                            && isBusinessHours
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

}
