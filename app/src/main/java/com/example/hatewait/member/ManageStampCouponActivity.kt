package com.example.hatewait.member

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_manage_stamp_coupon.*


class ManageStampCouponActivity : FragmentActivity() {
    private val stampCouponArray = arrayListOf<String>("STAMP", "COUPON")
    private lateinit var memberId: String
    private lateinit var storeId: String
    private lateinit var storeName: String
    private lateinit var stampBenefit: String
    private var maximumStamp: Int? = null
    private var stampCount: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_stamp_coupon)

//        setSupportActionBar(stamp_coupon_toolbar)
//        supportActionBar?.apply {
//            setDisplayHomeAsUpEnabled(true)
//            setHomeAsUpIndicator(R.drawable.back_icon)
//            setDisplayShowTitleEnabled(false)
//        }


        memberId = intent.getStringExtra("memberId")
        storeId = intent.getStringExtra("storeId")
        storeName = intent.getStringExtra("store_name")
        stampBenefit = intent.getStringExtra("stamp_benefit")
        stampCount = intent.getIntExtra("stamp_count", 0)
        maximumStamp = intent.getIntExtra("maximum_stamp", 1)

        stamp_coupon_title_text_view.text = "내 스탬프 / 쿠폰 - ${storeName}"

        init()
    }

    private fun init() {



        val bundle = Bundle()
        bundle.putString("memberId", memberId)
        bundle.putString("storeId", storeId)
        bundle.putString("store_name", storeName)
        bundle.putString("stamp_benefit", stampBenefit)
        bundle.putInt("stamp_count", stampCount!!)
        bundle.putInt("maximum_stamp", maximumStamp!!)

        val stampFragment = StampFragment()
        val couponFragment = CouponFragment()


        stampFragment.arguments = bundle
        couponFragment.arguments = bundle

        val fm: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fm.beginTransaction()
        fragmentTransaction.add(R.id.coupon_fragment, couponFragment)
        fragmentTransaction.add(R.id.stamp_fragment, stampFragment)
        fragmentTransaction.commit()






//        manage_stamp_coupon_view_pager.adapter = StampCouponFragAdapter(
//            this,
//            storeId,
//            storeName,
//            stampBenefit,
//            stampCount!!,
//            maximumStamp!!
//        )
////        manage_stamp_coupon_view_pager.adapter = StampCouponFragAdapter(this)
//
//
//        TabLayoutMediator(
//            manage_stamp_coupon_tab_layout,
//            manage_stamp_coupon_view_pager
//        ) { tab, position ->
//            tab.text = stampCouponArray[position]
//            // tab.icon
//        }.attach()
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


