package com.example.hatewait

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_register_tab_pager.*
import org.jetbrains.anko.support.v4.viewPager

//Recycler View Adpater
class LoginRegisterViewPagerActivity : AppCompatActivity() {
    private val tabNameArray = arrayOf<String>("비회원", "회원")
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_tab_pager)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()

    }
    private fun init() {
        view_pager.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(kind_of_member_tab_layout, view_pager) {
                tab, position ->
            tab.text = tabNameArray[position]
        }.attach()
        //attach() :    Link the TabLayout and the ViewPager2 together.
        // Must be called after ViewPager2 has an adapter set

    }
}
