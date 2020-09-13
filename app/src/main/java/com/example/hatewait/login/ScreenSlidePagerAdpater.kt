package com.example.hatewait.login

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hatewait.register.MemberRegister
import com.example.hatewait.register.NonMemberRegister

class ScreenSlidePagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
//        Nonmember + Member version = 2 page
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NonMemberRegister()
            1 -> MemberRegister()
            else -> NonMemberRegister()
        }
    }
}