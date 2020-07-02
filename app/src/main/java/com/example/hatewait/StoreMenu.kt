package com.example.hatewait

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.AsyncTask
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

    class storeMenuAsyncTask : AsyncTask<Unit, Unit, Array<String>?>() {

        private var clientSocket: Socket? = null
        private var reader: BufferedReader? = null // 서버 < 앱
        private var writer: PrintWriter? = null // 앱 > 서버

        private val port = 3000// port num

        var StoreMenuArray: Array<String>? = null

        override fun doInBackground(vararg params: Unit): Array<String>? { // 소켓 연결
            try {
                clientSocket = Socket(ip, port)
                Log.i("로그", "storeMenuAsyncTask:: ok")
                writer = PrintWriter(clientSocket!!.getOutputStream(), true)
                reader = BufferedReader(
                    InputStreamReader(
                        clientSocket!!.getInputStream(),
                        StandardCharsets.UTF_8
                    )
                )
                writer!!.println("MAIN;STORE;s0000")
                Log.i("로그:서버에게 정보 달라고 보냄", "MAIN;STORE;s1111")
                val storeMenuResponse: String = reader!!.readLine()
                Log.i("로그:서버응답", storeMenuResponse)
                //서버>앱: MAIN;STORE;storeName;waitingNum;nextName;nextNum
                StoreMenuArray = storeMenuResponse.split(";").toTypedArray()
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

            return StoreMenuArray
        }

        override fun onPostExecute(result: Array<String>??) { // UI에 보이기
            try {
                super.onPostExecute(result)
                Log.i("로그", " storeMenuAsyncTask-onPostExecute:: ok")
                //서버>앱: MAIN;STORE;storeName;waitingNum;nextName;nextNum

                storeNameView.setText(result?.get(2))
                storeWaitingNum.setText(result?.get(3))
                storeMarquee.setText(
                    String.format(
                        "다음 순서 : %s 외 %d명 입니다. 호출 버튼을 눌러 다음 대기자에게 알림을 보내주세요. 다음 순서 : %s 외 %d명 입니다. 호출 버튼을 눌러 다음 대기자에게 알림을 보내주세요",
                        result?.get(4),
                        result?.get(5)?.toInt(),
                        result?.get(4),
                        result?.get(5)?.toInt()
                    )
                )
                storeMarquee.setSelected(true) // 마키 텍스트에 포커스
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}




