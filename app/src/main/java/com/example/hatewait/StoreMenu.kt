package com.example.hatewait

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_store_menu.*
import org.jetbrains.anko.startActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

lateinit var storeName: String
lateinit var waitingNum: String
lateinit var nextName :String
lateinit var nextNum :String

lateinit var storeNameView  : TextView
lateinit var storeWaitingNum : TextView
lateinit var storeMarquee : TextView

class StoreMenu : AppCompatActivity() {


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_menu)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        init()

    }

    fun init() {
        setMenuBtn()
        storeMenuAsyncTask(this)
    }


    fun setMenuBtn() {

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

    class storeMenuAsyncTask(context: StoreMenu) : AsyncTask<Unit, Unit, Unit>() {

        private var clientSocket : Socket? = null
        private var reader: BufferedReader? = null // 서버 < 앱
        private var writer : PrintWriter? = null // 앱 > 서버

        private val port = 1818 // port num
        private val ip: String = R.string.serverIPAddress.toString()// 서버 ip적기
        val storeId = 12345678

        var StoreMenuArray: Array<String>? = null

        override fun doInBackground(vararg params: Unit?) { // 소켓 연결
            try {
                clientSocket = Socket(ip, port)
                writer = PrintWriter(clientSocket!!.getOutputStream(), true)
                reader = BufferedReader(
                    InputStreamReader(
                        clientSocket!!.getInputStream(),
                        "EUC_KR"
                    )
                )
                writer!!.println("MAIN;STORE;storeId")
                Log.i("로그:서버에게 정보 달라고 보냄", storeId.toString())
                val storeMenuResponse: String = reader!!.readLine()
                Log.i("로그:서버응답", storeMenuResponse)
                //서버>앱: MAIN;STORE;storeName;waitingNum;nextName;nextNum
                StoreMenuArray =storeMenuResponse.split(";").toTypedArray()
                if (reader != null) {
                    try {
                        reader!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                if (writer != null) {
                    writer!!.close()
                }
                if (clientSocket != null) {
                    try {
                        clientSocket!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun onPostExecute(result: Unit?) { // UI에 보이기
            super.onPostExecute(result)
            //서버>앱: MAIN;STORE;storeName;waitingNum;nextName;nextNum
            storeName = StoreMenuArray!!.get(2)
            waitingNum= StoreMenuArray!!.get(3)
            nextName= StoreMenuArray!!.get(4)
            nextNum= StoreMenuArray!!.get(5)

            storeNameView.setText(storeName)
            storeWaitingNum.setText(waitingNum)
            storeMarquee.setText(
            String.format(
                "다음 순서 : %s 외 %d명 입니다. 호출 버튼을 눌러 다음 대기자에게 알림을 보내주세요. 다음 순서 : %s 외 %d명 입니다. 호출 버튼을 눌러 다음 대기자에게 알림을 보내주세요",
                nextName,
                nextNum,
                nextName,
                nextNum
            ))
            storeMarquee.setSelected(true) // 마키 텍스트에 포커스
        }

    }


}




