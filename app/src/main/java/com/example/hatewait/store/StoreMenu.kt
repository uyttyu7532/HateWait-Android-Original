package com.example.hatewait.store

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R

import com.example.hatewait.login.LoginRegisterViewPagerActivity
import com.example.hatewait.socket.*
import com.example.hatewait.storeinfo.StoreInfoUpdate2
import kotlinx.android.synthetic.main.activity_store_menu.*
import org.jetbrains.anko.startActivity


lateinit var storeNameView: TextView
lateinit var storeWaitingNum: TextView
//lateinit var storeMarquee: TextView
lateinit var storeContext: Context

class StoreMenu : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_menu)

        storeContext = this.applicationContext

        // store mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        storeNameView = findViewById<TextView>(
            R.id.store_name_view
        )
        storeWaitingNum = findViewById<TextView>(
            R.id.store_waiting_num
        )
//        storeMarquee = findViewById<TextView>(
//            R.id.store_marquee
//        )
        // 임시 로그인 이후 ID와 이름만 받아옴.
        val storeReference =
            getSharedPreferences(resources.getString(R.string.store_mode), Context.MODE_PRIVATE)
        STOREID = storeReference.getString("STORE_ID", "")
        STORENAME = storeNameView.text.toString()
        initView()

    }

    override fun onResume() {
        super.onResume()
        StoreMenuAsyncTask().execute()

    }

    private fun initView() {
        tabletBtn.setOnClickListener {
            startActivity<LoginRegisterViewPagerActivity>()
        }

        listBtn.setOnClickListener {
            startActivity<StoreWaitingList>()
        }

        store_info_update_button2.setOnClickListener {
            startActivity<StoreInfoUpdate2>()
        }

        customer_list_button2.setOnClickListener{
//            startActivity<CustomerListActivity>()
//            startActivity<StoreInfoUpdate2>()
            startActivity<VisitorListActivity>()
        }
    }


    companion object {
        private const val LOG_TAG = "StoreMenu"
    }
}




