package com.example.hatewait

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_store_info_update.*
import org.jetbrains.anko.startActivityForResult

class StoreInfoUpdate : AppCompatActivity(), StoreNameChangeDialog.DialogListener, AutoCallNumberChangeDialog.DialogListener, StorePhoneNumberChgangeDialog.DialogListener  {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_info_update)
        init()
    }

    fun init() {

        store_name_edit_button.setOnClickListener {
            val storeNameChangeDialog = StoreNameChangeDialog()
            storeNameChangeDialog.show(supportFragmentManager, "STORE_NAME_CHANGE")
        }
        store_business_hours_text.setOnClickListener {
            val intent = Intent(this@StoreInfoUpdate, BusinessHourPick::class.java)
            startActivityForResult(intent, 2000)
        }
        auto_call_number.setOnClickListener {
            val autoCallNumberChangeDialog = AutoCallNumberChangeDialog()
            autoCallNumberChangeDialog.show(supportFragmentManager, "AUTO_CALL_NUMBER_CHANGE")
        }
        store_phone_number_textView.setOnClickListener {
            val storePhoneNumberChangeDialog = StorePhoneNumberChgangeDialog()
            storePhoneNumberChangeDialog.show(supportFragmentManager, "STORE_PHONE_CHANGE")
        }
//        store_phone_number_editText.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if (!verifyPhoneNumber(s.toString())) {
//                    store_phone_number_editText_layout.error = "9~11자리 전화번호를 입력해주세요"
//                } else {
//                    store_phone_number_editText_layout.error = null
//                }
////                하나라도 공백이 있을 경우 or 입력 양식에 맞지 않을 경우 수정 완료 버튼 비활성화
//                update_store_info_button.isEnabled =
//                    (!store_address_editText.text.isNullOrBlank()
//                            && !store_capacity_number_editText.text.isNullOrBlank()
//                            && !store_phone_number_editText.text.isNullOrBlank()
//                            && !store_business_hours_text.text.isNullOrBlank()
//                            && store_phone_number_editText_layout.error == null)
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//        })

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
