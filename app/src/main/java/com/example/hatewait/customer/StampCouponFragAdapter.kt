package com.example.hatewait.customer


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hatewait.customer.CouponFragment
import com.example.hatewait.customer.StampFragment

class StampCouponFragAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }


    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> StampFragment()
            else-> CouponFragment()
        }
    }
}