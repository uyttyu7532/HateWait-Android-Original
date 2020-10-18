package com.example.hatewait.storeinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R
import com.zyyoona7.wheel.WheelView


class SettingStampCoupon : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_stamp_coupon)


        val wheelView: WheelView<String> = findViewById(R.id.year_mon_day_wheel_view)
//        val list: MutableList<Int> = ArrayList(1)
//        for (i in 0..100) {
//            list.add()
//        }
        val list: MutableList<String> = ArrayList(1)
        list.add("년")
        list.add("개월")
        list.add("일")
        //设置数据
        //设置数据
        wheelView.data = list



    }
}