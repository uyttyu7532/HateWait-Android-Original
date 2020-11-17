package com.example.hatewait.storeinfo

import LottieDialogFragment.Companion.fragment
import LottieDialogFragment.Companion.newInstance
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.hatewait.R
import com.example.hatewait.address.AddressDialogFragment
import com.example.hatewait.login.LoginInfo.storeInfo
import com.example.hatewait.model.storeInfoData
import com.example.hatewait.retrofit2.MyApi
import com.example.hatewait.signup.addressDialog
import kotlinx.android.synthetic.main.activity_store_info_update2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreInfoUpdate2 : AppCompatActivity(), StoreNameChangeDialog.DialogListener,
    StorePhoneNumberChangeDialog.DialogListener, StoreCapacityNumberChangeDialog.DialogListener,
    StoreIntroduceChangeDialog.DialogListener {
    private val REQUEST_CODE_BUSINESS_TIME = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_info_update2)

        setSupportActionBar(store_info_update_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setDisplayShowTitleEnabled(false)
        }


        setting_store_name.setOnClickListener {
            StoreNameChangeDialog().show(supportFragmentManager, "STORE_NAME_CHANGE")
        }
        setting_password.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity1::class.java)
            intent.putExtra("USER_ID", storeInfo!!.id)
            intent.putExtra("IS_STORE", true)
            this.startActivity(intent)
        }
        setting_time.setOnClickListener {
            val intent = Intent(this@StoreInfoUpdate2, BusinessHourPick::class.java)
            startActivityForResult(intent, 200)
        }
        setting_phone_num.setOnClickListener {
            StorePhoneNumberChangeDialog()
                .show(supportFragmentManager, "STORE_PHONE_CHANGE")
        }
        setting_coupon.setOnClickListener {
            val intent = Intent(this, SettingStampCoupon::class.java)
            intent.putExtra("is_using_stamp_coupon", is_using_stamp_coupon_text_view.text)
            this.startActivity(intent)
        }
        setting_address.setOnClickListener {
            val bundle = Bundle()
            addressDialog = AddressDialogFragment().getInstance()

            addressDialog.callBack = {

                if (fragment == null || (!(fragment?.isAdded)!!)) {
                    newInstance().show(supportFragmentManager, "")
                }
                MyApi.UpdateService.requestStoreAddressUpdate(
                    id = storeInfo!!.id,
                    address = it
                ).enqueue(object : Callback<MyApi.onlyMessageResponseData> {
                    override fun onFailure(
                        call: Call<MyApi.onlyMessageResponseData>,
                        t: Throwable
                    ) {
                        Log.d("retrofit2 가게주소변경 :: ", "연결실패 $t")
                    }

                    override fun onResponse(
                        call: Call<MyApi.onlyMessageResponseData>,
                        response: Response<MyApi.onlyMessageResponseData>
                    ) {
                        newInstance().dismiss()
                        Log.d(
                            "retrofit2 가게주소변경 ::",
                            response.code().toString() + response.body().toString()
                        )
                        when (response.code()) {
                            204 -> {
                                setting_store_address_text_view.setText(it)
                            }
                        }
                    }
                }
                )

            }
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

    override fun onResume() {
        super.onResume()

        if (fragment == null || (!(fragment?.isAdded)!!)) {
            newInstance().show(supportFragmentManager, "")
        }
        MyApi.UpdateService.requestStoreInfo(storeInfo!!.id)
            .enqueue(object : Callback<storeInfoData> {
                override fun onFailure(call: Call<storeInfoData>, t: Throwable) {
                    Log.d("retrofit2 가게정보조회 :: ", "연결실패 $t")
                }

                override fun onResponse(
                    call: Call<storeInfoData>,
                    response: Response<storeInfoData>
                ) {
                    newInstance().dismiss()
                    var data: storeInfoData? = response?.body()
                    Log.d(
                        "retrofit2 가게정보조회 ::",
                        response.code().toString() + response.body().toString()
                    )
                    when (response.code()) {
                        200 -> {
                            setting_store_name_text_view.text = data!!.storeInfo.name
                            setting_store_phone_text_view.text = data!!.storeInfo.phone
                            setting_store_address_text_view.text = data!!.storeInfo.address
                            setting_store_capacity_text_view.text =
                                data!!.storeInfo.maximum_capacity.toString()
                            setting_store_info_text_view.text = data!!.storeInfo.info
                            setting_business_time.text = data!!.storeInfo.business_hour

                            when(data!!.storeInfo.coupon_enable){
                                1 -> is_using_stamp_coupon_text_view.text = "ON"
                                0 -> is_using_stamp_coupon_text_view.text = "OFF"
                            }
                        }
                    }
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_BUSINESS_TIME) {
            if (resultCode == 200) {

                if (fragment == null || (!(fragment?.isAdded)!!)) {
                    newInstance().show(supportFragmentManager, "")
                }
                MyApi.UpdateService.requestStoreBusinessHourUpdate(
                    id = storeInfo!!.id,
                    business_hour = data?.getStringExtra("UPDATED_BUSINESS_TIME").toString()
                ).enqueue(object : Callback<MyApi.onlyMessageResponseData> {
                    override fun onFailure(
                        call: Call<MyApi.onlyMessageResponseData>,
                        t: Throwable
                    ) {
                        Log.d("retrofit2 영업시간변경 :: ", "연결실패 $t")
                    }

                    override fun onResponse(
                        call: Call<MyApi.onlyMessageResponseData>,
                        response: Response<MyApi.onlyMessageResponseData>
                    ) {
                        newInstance().dismiss()
                        Log.d(
                            "retrofit2 영업시간변경 ::",
                            response.code().toString() + response.body().toString()
                        )
                        when (response.code()) {
                            204 -> {
                                var data: MyApi.onlyMessageResponseData? =
                                    response?.body() // 서버로부터 온 응답
                            }
                        }
                    }
                }
                )
            }
            if (resultCode == 400) {
//                nothing to do (failed to update business Time)
            }
        }
    }

    override fun applyStoreName(storeName: String) {
        setting_store_name_text_view.text = storeName
    }

    override fun applyPhoneNumber(storePhoneNumber: String) {
        setting_store_phone_text_view.text = storePhoneNumber
    }

    override fun applyCapacityNumber(capacityNumber: String) {
        setting_store_capacity_text_view.text = capacityNumber
    }

    override fun applyStoreIntroduce(storeIntroduce: String) {
        setting_store_info_text_view.text = storeIntroduce
    }
}
