package com.example.hatewait

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import kotlinx.android.synthetic.main.activity_store_info_update.*
import org.jetbrains.anko.startActivityForResult

class StoreInfoUpdate : AppCompatActivity(),
    StoreNameChangeDialog.DialogListener, AutoCallNumberChangeDialog.DialogListener,
    StorePhoneNumberChgangeDialog.DialogListener, StoreCapacityNumberChangeDialog.DialogListener  {

    //    3자리 - 3 or 4자리 - 4자리
    //    첫자리는 반드시 0으로 시작.
    private val storePhoneRegex = Regex("^[0](\\d{1,2})(\\d{3,4})(\\d{4})")
    fun verifyPhoneNumber (input_phone_number : String) : Boolean = input_phone_number.matches(storePhoneRegex)
    private val REQUEST_CODE_BUSINESS_TIME = 2000


//    interface class (DialogListener) function implements
    override fun applyText(storeName: String) {
        store_name.text = storeName
    }

    override fun applyPhoneNumber(storePhoneNumber: String) {
        store_phone_number_textView.text = storePhoneNumber
    }

    override fun applyPickedNumber(autoCallNumber: String) {
        auto_call_number.text = autoCallNumber
    }

    override fun applyCapacityNumber(capacityNumber: String) {
        store_capacity_number_textView.text = capacityNumber
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_info_update)
        init()
    }


    fun init() {

//        DB로부터 불러온 변경 전 가게정보
        val initialStoreInfoMap : Map<String, String>
                = mapOf(Pair("AUTO_CALL", auto_call_number.text.toString()),
                    Pair("ADDRESS", store_address_editText.text.toString()),
                    Pair("CAPACITY", store_capacity_number_textView.text.toString()),
                    Pair("PHONE", store_phone_number_textView.text.toString()),
                    Pair("TIME", store_business_hours_text.text.toString())
                )
        store_name_edit_button.setOnClickListener {
            StoreNameChangeDialog().show(supportFragmentManager, "STORE_NAME_CHANGE")
        }
        store_business_hours_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(updatedBusinessHours: Editable?) {
                update_store_info_button.isEnabled =
                    initialStoreInfoMap["TIME"] != updatedBusinessHours.toString()
                    || initialStoreInfoMap["AUTO_CALL"] != auto_call_number.toString()
                    || initialStoreInfoMap["ADDRESS"] != store_address_editText.text.toString()
                    || initialStoreInfoMap["CAPACITY"] != store_capacity_number_textView.text.toString()
                    || initialStoreInfoMap["PHONE"] != store_phone_number_textView.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        store_business_hours_text.setOnClickListener {
            val intent = Intent(this@StoreInfoUpdate, BusinessHourPick::class.java)
            startActivityForResult(intent, 2000)
        }
        auto_call_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(updatedCallNumber: Editable?) {
//                current Value (DB로부터 받아온 값)을 쥐고 비교해서 달라지면 버튼 활성화 (달라지는거 없으면 버튼바뀔이유 X)
                    update_store_info_button.isEnabled =
                        initialStoreInfoMap["AUTO_CALL"] != updatedCallNumber.toString()
                        || initialStoreInfoMap["ADDRESS"] != store_address_editText.text.toString()
                        || initialStoreInfoMap["CAPACITY"] != store_capacity_number_textView.text.toString()
                        || initialStoreInfoMap["PHONE"] != store_phone_number_textView.text.toString()
                        || initialStoreInfoMap["TIME"] != store_business_hours_text.text.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        auto_call_number.setOnClickListener {
            AutoCallNumberChangeDialog().show(supportFragmentManager, "AUTO_CALL_NUMBER_CHANGE")
        }
        store_capacity_number_textView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(updatedCapacity: Editable?) {
                update_store_info_button.isEnabled =
                    initialStoreInfoMap["CAPACITY"] != updatedCapacity.toString()
                    || initialStoreInfoMap["AUTO_CALL"] != auto_call_number.toString()
                    || initialStoreInfoMap["ADDRESS"] != store_address_editText.text.toString()
                    || initialStoreInfoMap["PHONE"] != store_phone_number_textView.text.toString()
                    || initialStoreInfoMap["TIME"] != store_business_hours_text.text.toString()
                Log.i("update", updatedCapacity.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        store_capacity_number_textView.setOnClickListener {
            StoreCapacityNumberChangeDialog().show(supportFragmentManager, "STORE_CAPACITY_CHANGE")
        }
        store_phone_number_textView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(updatedPhoneNumber: Editable?) {
                update_store_info_button.isEnabled =
                    initialStoreInfoMap["PHONE"] != updatedPhoneNumber.toString()
                    || initialStoreInfoMap["AUTO_CALL"] != auto_call_number.toString()
                    || initialStoreInfoMap["CAPACITY"] != store_capacity_number_textView.text.toString()
                    || initialStoreInfoMap["ADDRESS"] != store_address_editText.text.toString()
                    || initialStoreInfoMap["TIME"] != store_business_hours_text.text.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        store_phone_number_textView.setOnClickListener {
            StorePhoneNumberChgangeDialog().show(supportFragmentManager, "STORE_PHONE_CHANGE")
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
