package com.example.hatewait.memberinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hatewait.R
import com.example.hatewait.login.memberInfo
import com.example.hatewait.login.storeInfo
import com.example.hatewait.model.MemberInfoData
import com.example.hatewait.model.storeInfoData
import com.example.hatewait.retrofit2.MyApi
import com.example.hatewait.storeinfo.ChangePasswordActivity1
import com.example.hatewait.storeinfo.StorePhoneNumberChangeDialog
import kotlinx.android.synthetic.main.activity_customer_info_update.*
import kotlinx.android.synthetic.main.activity_store_info_update2.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    override fun onResume() {
        super.onResume()

        // 아직 테스트 못함
        MyApi.UpdateService.requestMemberInfo(memberInfo!!.id)
            .enqueue(object : Callback<MemberInfoData> {
                override fun onFailure(call: Call<MemberInfoData>, t: Throwable) {
                    Log.d("retrofit2 손님정보조회 :: ", "연결실패 $t")
                }

                override fun onResponse(
                    call: Call<MemberInfoData>,
                    response: Response<MemberInfoData>
                ) {
                    var data: MemberInfoData? = response?.body()
                    Log.d(
                        "retrofit2 손님정보조회 ::",
                        response.code().toString() + response.body().toString()
                    )
                    when (response.code()) {
                        200 -> {
                            setting_member_name_text_view.text = data!!.memberInformation.name
                            setting_member_email_text_view.text = data!!.memberInformation.email
                            setting_member_phone_text_view.text = data!!.memberInformation.phone
                        }
                    }
                }
            })
    }
}