package com.example.hatewait

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_register_tab_pager.*

//Recycler View Adpater
class LoginRegisterViewPagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_tab_pager)
        kind_of_member_tab_layout.addTab(kind_of_member_tab_layout.newTab().setText("비회원 로그인"))
        kind_of_member_tab_layout.addTab(kind_of_member_tab_layout.newTab().setText("회원 로그인"))

        val pagerAdapter = PagerAdapter(supportFragmentManager, tabCount = 2)
        view_pager.adapter = pagerAdapter

//        ViewPager2 버전 사용시.. 하
//        TabLayoutMediator(kind_of_member_tab_layout, view_pager) {
//                tab, position ->  Log.d("this position: ", (position + 1).toString())
//        }.attach()
        kind_of_member_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    view_pager.currentItem = tab!!.position
                }
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }
            })

        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(kind_of_member_tab_layout))

    }
}



class PagerAdapter (fragmentManager: FragmentManager, val tabCount: Int) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0-> return NonMemberRegister()
            1-> return MemberRegister()
            else -> return NonMemberRegister()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}