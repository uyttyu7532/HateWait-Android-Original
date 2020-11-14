package com.example.hatewait.storeinfo

import LottieDialogFragment.Companion.newInstance
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View.*
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R
import com.example.hatewait.login.storeInfo
import com.example.hatewait.model.CouponEnableRequestData
import com.example.hatewait.model.CouponInfoData
import com.example.hatewait.model.CouponResponseData
import com.example.hatewait.model.CouponUnableRequestData
import com.example.hatewait.retrofit2.MyApi
import com.zyyoona7.wheel.WheelView
import kotlinx.android.synthetic.main.activity_setting_stamp_coupon.*
import kotlinx.android.synthetic.main.activity_store_signup4.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SettingStampCoupon : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_stamp_coupon)

        setSupportActionBar(setting_stamp_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setDisplayShowTitleEnabled(false)
        }

        val wheelView: WheelView<String> = findViewById(R.id.year_mon_day_wheel_view)
        val wheelList: MutableList<String> = ArrayList(1)
        wheelList.add("년 (365일)")
        wheelList.add("개월 (30일)")
        wheelList.add("일")
        wheelView.data = wheelList

        newInstance().show(supportFragmentManager, "")
        MyApi.CouponService.requestStoreCouponInfo(
            storeInfo!!.id
        )
            .enqueue(object : Callback<CouponResponseData?> {
                override fun onFailure(
                    call: Call<CouponResponseData?>,
                    t: Throwable
                ) {
                    Log.d("retrofit2 쿠폰설정정보조회 :: ", "연결실패 $t")
                }

                override fun onResponse(
                    call: Call<CouponResponseData?>,
                    response: Response<CouponResponseData?>
                ) {
                    newInstance().dismiss()
                    var data: CouponResponseData? = response?.body()
                    Log.d(
                        "retrofit2 쿠폰설정정보조회 ::",
                        response.code().toString() + response.body().toString()
                    )
                    when (response.code()) {
                        200 -> {
                            number_picker_stamp.value = data!!.couponInformation.maximum_stamp
                            benefit_description_text_view.setText(data!!.couponInformation.benefit_description)
                            when {
                                data!!.couponInformation.validity_period_days%365==0 -> {
                                    number_picker_date.value = data!!.couponInformation.validity_period_days/365
                                    year_mon_day_wheel_view.setSelectedItemPosition(0)
                                }
                                data!!.couponInformation.validity_period_days%30==0 -> {
                                    number_picker_date.value = data!!.couponInformation.validity_period_days/30
                                    year_mon_day_wheel_view.setSelectedItemPosition(1)
                                }
                                else -> {
                                    number_picker_date.value = data!!.couponInformation.validity_period_days
                                    year_mon_day_wheel_view.setSelectedItemPosition(2)
                                }
                            }
                            remark_text_view.setText(data!!.couponInformation.remark)
                        }
                    }
                }
            })

        var isUsingStampCoupon = intent.getStringExtra("is_using_stamp_coupon")

        when (isUsingStampCoupon) {
            "ON" -> {
                btn_spotify.isEnabled = false
                setting_stamp_switch.isChecked = true
                setting_coupon_scroll_view.visibility = VISIBLE
            }
            "OFF" -> {
                btn_spotify.isChecked = false
                setting_coupon_scroll_view.visibility = INVISIBLE
            }
        }

        setting_stamp_switch.setOnCheckedChangeListener { _: CompoundButton, checked: Boolean ->
            when (checked) {
                true -> {
                    setting_coupon_scroll_view.visibility = VISIBLE
                    btn_spotify.isEnabled =
                        benefit_description_layout.error == null
                                && remark_layout.error == null
                                && !benefit_description_text_view.text.isNullOrEmpty()
                                && !remark_text_view.text.isNullOrEmpty()

                    if (setting_stamp_switch.isChecked) {
                        benefit_description_text_view.addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(benefit_description: Editable?) {
                                btn_spotify.isEnabled =
                                    !benefit_description_text_view.text!!.isNotBlank()
                                            && !remark_text_view.text!!.isNotBlank()
                            }

                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                if (s.isNullOrEmpty()) {
                                    benefit_description_layout.error = "쿠폰 혜택을 입력해주세요."
                                    btn_spotify.isEnabled = false
                                } else {
                                    benefit_description_layout.isErrorEnabled=false
                                    benefit_description_layout.isHintEnabled=false
                                    benefit_description_text_view.clearFocus()
                                    benefit_description_text_view.hint = null
                                    benefit_description_text_view.error = null
                                    benefit_description_layout.error = null
                                    benefit_description_layout.hint = null
                                }
                            }
                        })

                        remark_text_view.addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(remark: Editable?) {

                            }

                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                if (s.isNullOrEmpty()) {
                                    remark_layout.error = "쿠폰 유의사항을 입력해주세요."
                                    btn_spotify.isEnabled = false
                                } else {
                                    remark_layout.isErrorEnabled=false
                                    remark_layout.isHintEnabled=false
                                    remark_layout.error = null
                                    remark_layout.hint = ""
                                    remark_text_view.error = null
                                }
                                btn_spotify.isEnabled =
                                    !benefit_description_text_view.text!!.isNotBlank()
                                            && !remark_text_view.text!!.isNotBlank()

                            }
                        })
                    }
                }
                else -> {
                    btn_spotify.isEnabled = true
                    setting_coupon_scroll_view.visibility = GONE
                }
            }
        }




        btn_spotify.setOnClickListener {

            Log.d("retrofit2 스위치상태 :: ", "${setting_stamp_switch.isChecked}")

            // 스위치 off 일때
            if (!setting_stamp_switch.isChecked) {

                newInstance().show(supportFragmentManager, "")
                MyApi.UpdateService.requestStoreCouponUnableUpdate(
                    CouponUnableRequestData(storeInfo!!.id)
                )
                    .enqueue(object : Callback<MyApi.onlyMessageResponseData> {
                        override fun onFailure(
                            call: Call<MyApi.onlyMessageResponseData>,
                            t: Throwable
                        ) {
                            Log.d("retrofit2 쿠폰설정해제 :: ", "연결실패 $t")
                        }

                        override fun onResponse(
                            call: Call<MyApi.onlyMessageResponseData>,
                            response: Response<MyApi.onlyMessageResponseData>
                        ) {
                            newInstance().dismiss()
                            var data: MyApi.onlyMessageResponseData? = response?.body()
                            Log.d(
                                "retrofit2 쿠폰설정해제 ::",
                                response.code().toString() + response.body().toString()
                            )
                            when (response.code()) {
                                204 -> {
                                    finish()
                                }
                            }
                        }
                    })
            } else {
                var selectedPeriod: Int = when (year_mon_day_wheel_view.selectedItemData) {
                    "개월 (30일)" -> 30
                    "년 (365일)" -> 365
                    else -> 1
                }

                newInstance().show(supportFragmentManager, "")
                MyApi.UpdateService.requestStoreCouponEnableUpdate(
                    CouponEnableRequestData(
                        id = storeInfo!!.id, coupon_information = CouponInfoData(
                            benefit_description_text_view.text.toString(),
                            number_picker_stamp.value,
                            number_picker_date.value * selectedPeriod,
                            remark_text_view.text.toString()
                        )
                    )

                )
                    .enqueue(object : Callback<MyApi.onlyMessageResponseData> {
                        override fun onFailure(
                            call: Call<MyApi.onlyMessageResponseData>,
                            t: Throwable
                        ) {
                            Log.d("retrofit2 쿠폰설정 :: ", "연결실패 $t")
                        }

                        override fun onResponse(
                            call: Call<MyApi.onlyMessageResponseData>,
                            response: Response<MyApi.onlyMessageResponseData>
                        ) {
                            newInstance().dismiss()
                            var data: MyApi.onlyMessageResponseData? = response?.body()
                            Log.d(
                                "retrofit2 쿠폰설정 ::",
                                response.code().toString() + response.body().toString()
                            )
                            when (response.code()) {
                                204 -> {
                                    finish()
                                }
                            }
                        }
                    })

            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }


}
