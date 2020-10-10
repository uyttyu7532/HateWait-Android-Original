package com.example.hatewait.storeinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.hatewait.*
import com.example.hatewait.address.AddressDialogFragment
import com.example.hatewait.signup.addressDialog
import com.example.hatewait.socket.STOREID
import com.example.hatewait.socket.STORENAME
import com.example.hatewait.socket.StoreInfoUpdateAsyncTask
import kotlinx.android.synthetic.main.activity_store_info_update.*

class StoreInfoUpdate : AppCompatActivity(),
    AddressDialogFragment.AddressDialogListener {

    //    3자리 - 3 or 4자리 - 4자리
    //    첫자리는 반드시 0으로 시작.
    private val storePhoneRegex = Regex("^[0](\\d{1,2})(\\d{3,4})(\\d{4})")
    fun verifyPhoneNumber(input_phone_number: String): Boolean =
        input_phone_number.matches(storePhoneRegex)

    private val REQUEST_CODE_BUSINESS_TIME = 2000


//    //    interface class (DialogListener) function implements
//    override fun applyText(storeName: String) {
//        store_name.setText(storeName)
//    }
//
//    override fun applyPhoneNumber(storePhoneNumber: String) {
//        store_phone_number_textView.setText(storePhoneNumber)
//    }
//
//
//    override fun applyCapacityNumber(capacityNumber: String) {
//        store_capacity_number_textView.setText(capacityNumber)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_info_update)
        init()
    }


    fun init() {
        store_address_editText.setOnClickListener {
            val bundle = Bundle()
            addressDialog = AddressDialogFragment().getInstance()
            addressDialog.arguments = bundle
            supportFragmentManager?.let { fragmentManager ->
                addressDialog.show(
                    fragmentManager,
                    "SELECT_ADDRESS"
                )

            }
        }
//        임시
        store_name.setText(STORENAME)
//        DB로부터 불러온 변경 전 가게정보
        val initialStoreInfoMap: Map<String, String> = mapOf(
            Pair("NAME", store_name.text.toString()),
            Pair("ADDRESS", store_address_editText.text.toString()),
            Pair("CAPACITY", store_capacity_number_textView.text.toString()),
            Pair("PHONE", store_phone_number_textView.text.toString()),
            Pair("TIME", store_business_hours_text.text.toString())
        )
        update_store_info_button.setOnClickListener {
//            헛점: DB 쿼리가 MODIFY로 지정되어있어 모든 것들을.. 동기화해야함.
            val updatedStoreInfoMap: Map<String, String> =
                mapOf(
                    Pair("ID", STOREID),
                    Pair("PASSWORD", "updated12!@"),
                    Pair("NAME", store_name.text.toString()),
                    Pair("DESCRIPTION", "짱짱 맛있는 집"),
                    Pair("CAPACITY", store_capacity_number_textView.text.toString()),
                    Pair("BUSINESS_HOURS", store_business_hours_text.text.toString()),
                    Pair("ADDRESS", store_address_editText.text.toString()),
                    Pair("PHONE", store_phone_number_textView.text.toString())
                )
            StoreInfoUpdateAsyncTask(this@StoreInfoUpdate).execute(updatedStoreInfoMap)
        }

        store_name.setOnClickListener {
            StoreNameChangeDialog()
                .show(supportFragmentManager, "STORE_NAME_CHANGE")
        }

        store_business_hours_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(updatedBusinessHours: Editable?) {
                update_store_info_button.isEnabled =
                    initialStoreInfoMap["TIME"] != updatedBusinessHours.toString()
                            || initialStoreInfoMap["NAME"] != store_name.text.toString()
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


        store_capacity_number_textView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(updatedCapacity: Editable?) {
                update_store_info_button.isEnabled =
                    initialStoreInfoMap["CAPACITY"] != updatedCapacity.toString()
                            || initialStoreInfoMap["NAME"] != store_name.text.toString()
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
            StoreCapacityNumberChangeDialog()
                .show(supportFragmentManager, "STORE_CAPACITY_CHANGE")
        }
        store_phone_number_textView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(updatedPhoneNumber: Editable?) {
                update_store_info_button.isEnabled =
                    initialStoreInfoMap["PHONE"] != updatedPhoneNumber.toString()
                            || initialStoreInfoMap["NAME"] != store_name.text.toString()
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
            StorePhoneNumberChgangeDialog()
                .show(supportFragmentManager, "STORE_PHONE_CHANGE")
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



    override fun dismissDialog(dialog: DialogFragment) {
        Toast.makeText(this, "다이얼로그 닫고싶어요", Toast.LENGTH_SHORT).show()
        dialog.dismiss()
    }
}
