package com.example.hatewait.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hatewait.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_manage_stamp_coupon.*
import kotlinx.android.synthetic.main.activity_visitor_list.*
import kotlinx.android.synthetic.main.activity_visitor_list.visitor_view_pager

class ManageStampCouponActivity : AppCompatActivity() {
    val stampCouponArray = arrayListOf<String>("STAMP", "COUPON")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_stamp_coupon)
        init()
    }

    private fun init() {
        manage_stamp_coupon_view_pager.adapter = StampCouponFragAdapter(this)
        TabLayoutMediator(
            manage_stamp_coupon_tab_layout,
            manage_stamp_coupon_view_pager
        ) { tab, position ->
            tab.text = stampCouponArray[position]
            // tab.icon
        }.attach()
    }
}