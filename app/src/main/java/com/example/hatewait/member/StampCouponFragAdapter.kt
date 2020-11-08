package com.example.hatewait.member


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class StampCouponFragAdapter(fragmentActivity: FragmentActivity , var storeId: String, var stampCount: Int , var maximumStamp:Int) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }


    override fun createFragment(position: Int): Fragment {
        var bundle = Bundle()
        bundle.putString("store_id", storeId)
        bundle.putInt("stamp_count", stampCount)
        bundle.putInt("maximum_stamp", maximumStamp)


        var stampFragment = StampFragment()
        stampFragment.arguments = bundle

        var couponFragment = CouponFragment()
        couponFragment.arguments = bundle

        return when(position){
            0 -> stampFragment
            else-> couponFragment
        }
    }




}