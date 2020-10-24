package com.example.hatewait.storeinfo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R
import com.zyyoona7.wheel.WheelView
import kotlinx.android.synthetic.main.activity_setting_stamp_coupon.*
import kotlinx.android.synthetic.main.activity_store_info_update2.*
import kotlinx.android.synthetic.main.activity_store_info_update2.store_info_update_toolbar


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
//        val list: MutableList<Int> = ArrayList(1)
//        for (i in 0..100) {
//            list.add()
//        }
        val list: MutableList<String> = ArrayList(1)
        list.add("년")
        list.add("개월")
        list.add("일")

        wheelView.data = list

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