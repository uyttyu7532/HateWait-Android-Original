package com.example.hatewait.socket

import android.os.AsyncTask
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.example.hatewait.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class CustomerMenuAsyncTask : AsyncTask<String?, Unit, Array<String>?>() {

    private var CustomerMenuArray: Array<String>? = null

    override fun doInBackground(vararg params: String?): Array<String>? { // 소켓 연결
        try {
            clientSocket = Socket(SERVERIP, PORT)
            Log.i("로그", " customerMenuAsyncTask:: ok")
            writer = PrintWriter(clientSocket!!.getOutputStream(), true)
            reader = BufferedReader(
                InputStreamReader(
                    clientSocket!!.getInputStream(),
                    StandardCharsets.UTF_8
                )
            )
            writer!!.println("MAIN;MEMBER;${CUSTOMERID}")
            Log.i("로그", "서버에 보냄: " + "MAIN;MEMBER;${CUSTOMERID}")
            val storeMenuResponse: String = reader!!.readLine()
            Log.i("로그", "서버응답: " + storeMenuResponse)
            CustomerMenuArray = storeMenuResponse.split(";").toTypedArray()
            Log.i("로그", "CustomerMenu > Server::" + CustomerMenuArray.toString())
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
            Log.i("로그", " customerMenuAsyncTask: ${e}")
        }
        return CustomerMenuArray
    }

    override fun onPostExecute(result: Array<String>?) { // UI에 보이기
        if (result != null) {
            try {
                super.onPostExecute(result)
                Log.i("로그", " customerMenuAsyncTask-onPostExecute:: ok")
                //서버>앱: MAIN;MEMBER;customerName;waitingStoreName;waitingMyTurn

                Log.i("로그 result?.get(3):", result?.get(3))
                if (result?.get(3) == "null") {
                    waitingStoreView.text = "대기중인 가게가 없습니다."
                    customerWaiting.visibility = GONE
                } else {
                    customerNameView.text = result?.get(2)
                    waitingStoreView.text = result?.get(3)
                    customerWaitingNum.text = result?.get(4)
                    customerWaiting.visibility = VISIBLE
                }


                var reservationTime = System.currentTimeMillis() + 1000 * 60 * 60 * 9
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                val curTime = dateFormat.format(Date(reservationTime))
                recentRefreshTime.text = "최근 새로고침 시간: ${curTime}"

            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("로그", " customerMenuAsyncTask-onPostExecute:: ${e}")
            }
        }
    }
}