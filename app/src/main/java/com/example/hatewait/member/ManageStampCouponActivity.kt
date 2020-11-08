package com.example.hatewait.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.hatewait.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_manage_stamp_coupon.*



class ManageStampCouponActivity : AppCompatActivity() {
    private val stampCouponArray = arrayListOf<String>("STAMP", "COUPON")
    private lateinit var memberId: String
    private lateinit var storeId: String
    private var maximumStamp: Int? = null
    private var stampCount: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_stamp_coupon)

        setSupportActionBar(stamp_coupon_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setDisplayShowTitleEnabled(false)
        }


        memberId = intent.getStringExtra("memberId")
        storeId = intent.getStringExtra("storeId")
        stampCount = intent.getIntExtra("stamp_count",0)
        maximumStamp = intent.getIntExtra("maximum_stamp",1)


        init()
    }

    private fun init() {


        manage_stamp_coupon_view_pager.adapter = StampCouponFragAdapter(this, storeId, stampCount!!,maximumStamp!!)
//        manage_stamp_coupon_view_pager.adapter = StampCouponFragAdapter(this)


        TabLayoutMediator(
            manage_stamp_coupon_tab_layout,
            manage_stamp_coupon_view_pager
        ) { tab, position ->
            tab.text = stampCouponArray[position]
            // tab.icon
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }


}


