package com.example.hatewait

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.socket.*
import kotlinx.android.synthetic.main.activity_store_menu.*
import org.jetbrains.anko.startActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets

lateinit var storeNameView: TextView
lateinit var storeWaitingNum: TextView
lateinit var storeMarquee: TextView

class StoreMenu : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_menu)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        storeNameView = findViewById(R.id.store_name_view) as TextView
        storeWaitingNum = findViewById(R.id.store_waiting_num) as TextView
        storeMarquee = findViewById(R.id.store_marquee) as TextView

        initView()

    }

    override fun onResume() {
        super.onResume()
        storeMenuAsyncTask().execute()
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




