package com.example.hatewait

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.socket.*
import kotlinx.android.synthetic.main.activity_store_menu.*
import org.jetbrains.anko.startActivity


lateinit var storeNameView: TextView
lateinit var storeWaitingNum: TextView
lateinit var storeMarquee: TextView
lateinit var storeContext:Context

class StoreMenu : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_menu)

        storeContext = this.applicationContext

        //store mode
        val storeReference = getSharedPreferences(resources.getString(R.string.store_mode), Context.MODE_PRIVATE)
        STOREID = storeReference.getString("STORE_ID","")

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        storeNameView = findViewById(R.id.store_name_view) as TextView
        storeWaitingNum = findViewById(R.id.store_waiting_num) as TextView
        storeMarquee = findViewById(R.id.store_marquee) as TextView

        initView()

    }

    override fun onResume() {
        super.onResume()
        StoreMenuAsyncTask().execute()

    }

    fun initView() {
        tabletBtn.setOnClickListener {
            startActivity<LoginRegisterViewPagerActivity>()
        }

        listBtn.setOnClickListener {
            startActivity<StoreWaitingList>()
        }

        store_info_update_button2.setOnClickListener {
            startActivity<StoreInfoUpdate>()
        }
    }


}




