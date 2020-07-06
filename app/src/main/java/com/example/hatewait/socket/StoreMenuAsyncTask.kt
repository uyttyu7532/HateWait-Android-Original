package com.example.hatewait.socket

import android.os.AsyncTask
import android.util.Log
import com.example.hatewait.storeMarquee
import com.example.hatewait.storeNameView
import com.example.hatewait.storeWaitingNum
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets

class StoreMenuAsyncTask : AsyncTask<Unit, Unit, Array<String>?>() {

    var StoreMenuArray: Array<String>? = null

    override fun doInBackground(vararg params: Unit): Array<String>? { // 소켓 연결
        try {
            clientSocket = Socket(SERVERIP, PORT)
            Log.i("로그", "storeMenuAsyncTask:: ok")
            writer = PrintWriter(clientSocket!!.getOutputStream(), true)
            reader = BufferedReader(
                InputStreamReader(
                    clientSocket!!.getInputStream(),
                    StandardCharsets.UTF_8
                )
            )
            writer!!.println("MAIN;STORE;${STOREID}")
            Log.i("로그:서버에게 정보 달라고 보냄", "MAIN;STORE;${STOREID}")
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
        if (result != null) {
            try {
                super.onPostExecute(result)
                Log.i("로그", " storeMenuAsyncTask-onPostExecute:: ok")
                //서버>앱: MAIN;STORE;storeName;waitingNum;nextName;nextNum
                STORENAME = result?.get(2)
                storeNameView.text = result?.get(2)
                storeWaitingNum.text = result?.get(3)
                storeMarquee.text = String.format(
                    "다음 순서 : %s 외 %d명 입니다. 호출 버튼을 눌러 다음 대기자에게 알림을 보내주세요. 다음 순서 : %s 외 %d명 입니다. 호출 버튼을 눌러 다음 대기자에게 알림을 보내주세요",
                    result?.get(4),
                    result?.get(5)?.toInt(),
                    result?.get(4),
                    result?.get(5)?.toInt()
                )
                storeMarquee.isSelected = true // 마키 텍스트에 포커스
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("로그", " storeMenuAsyncTask-onPostExecute:: ${e}")
            }
        } else {
            storeNameView.text = STORENAME
            storeWaitingNum.text = "0"
            storeMarquee.text = "서버 연결을 확인해주세요."
            storeMarquee.isSelected = true // 마키 텍스트에 포커스
        }
    }
}