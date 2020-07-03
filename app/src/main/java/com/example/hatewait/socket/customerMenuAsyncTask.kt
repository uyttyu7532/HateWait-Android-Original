package com.example.hatewait.socket

import android.os.AsyncTask
import android.util.Log
import com.example.hatewait.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class customerMenuAsyncTask : AsyncTask<Unit, Unit, Array<String>?>() {

    var CustomerMenuArray: Array<String>? = null

    override fun doInBackground(vararg params: Unit?): Array<String>? { // 소켓 연결
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
        super.onPostExecute(result)
        Log.i("로그", " customerMenuAsyncTask-onPostExecute:: ok")
        //서버>앱: MAIN;MEMBER;customerName;waitingStoreName;waitingMyTurn

        customerNameView.setText(result?.get(2) ?: "김손님")
        waitingStoreView.setText(result?.get(3) ?: "대기중인 가게가 없습니다.")
        customerWaitingNum.setText(result?.get(4) ?: "0")
        customerMarquee.setText("내 차례가 되면 상태바 알림과 문자 알림이 발송됩니다. 취소 버튼을 눌러 대기를 취소할 수 있습니다. ")
        customerMarquee.setSelected(true) // 마키 텍스트에 포커스

        var reservationTime = System.currentTimeMillis() + 1000*60*60*9
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val curTime = dateFormat.format(Date(reservationTime))
        recentRefreshTime.text = "최근 새로고침 시간: ${curTime}"
    }
}