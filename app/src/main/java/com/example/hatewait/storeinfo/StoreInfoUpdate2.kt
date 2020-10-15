package com.example.hatewait.storeinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hatewait.R
import com.example.hatewait.address.AddressDialogFragment
import com.example.hatewait.signup.addressDialog
import kotlinx.android.synthetic.main.activity_store_info_update2.*

class StoreInfoUpdate2 : AppCompatActivity(), StoreNameChangeDialog.DialogListener,
    StorePhoneNumberChangeDialog.DialogListener, StoreCapacityNumberChangeDialog.DialogListener,StoreIntroduceChangeDialog.DialogListener {
    private val REQUEST_CODE_BUSINESS_TIME = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_info_update2)

        setting_store_name.setOnClickListener {
            StoreNameChangeDialog().show(supportFragmentManager, "STORE_NAME_CHANGE")
        }
        setting_password.setOnClickListener {

        }
        setting_time.setOnClickListener {
            val intent = Intent(this@StoreInfoUpdate2, BusinessHourPick::class.java)
            startActivityForResult(intent, 2000)
        }
        setting_phone_num.setOnClickListener {
            StorePhoneNumberChangeDialog()
                .show(supportFragmentManager, "STORE_PHONE_CHANGE")
        }
        setting_coupon.setOnClickListener {

        }
        setting_address.setOnClickListener {
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
        setting_capacity.setOnClickListener {
            StoreCapacityNumberChangeDialog()
                .show(supportFragmentManager, "STORE_CAPACITY_CHANGE")
        }
        setting_introduce.setOnClickListener {
            StoreIntroduceChangeDialog()
                .show(supportFragmentManager, "STORE_INTRODUCE_CHANGE")
        }








    }

    override fun applyText(storeName: String) {
        TODO("Not yet implemented")
    }

    override fun applyPhoneNumber(storePhoneNumber: String) {
        TODO("Not yet implemented")
    }

    override fun applyCapacityNumber(capacityNumber: String) {
        TODO("Not yet implemented")
    }
}
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUEST_CODE_BUSINESS_TIME) {
//            if (resultCode == 200) {
//                store_business_hours_text.text = data?.getStringExtra("UPDATED_BUSINESS_TIME")
//            }
//            if (resultCode == 400) {
////                nothing to do (failed to update business Time)
//            }
//        }
//    }
