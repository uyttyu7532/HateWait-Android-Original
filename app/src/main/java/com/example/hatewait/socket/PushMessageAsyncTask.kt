package com.example.hatewait.socket

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets


class PushMessageAsyncTask : AsyncTask<String, Unit, Unit>() {

    override fun doInBackground(vararg params: String) { // 소켓 연결
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

            // TODO 문자를 받을 손님 id 주석 풀기
             writer!!.println("PUSHMSG;${params[0]}")
            Log.i("로그:서버에게 정보 달라고 보냄", "PUSHMSG;${params[0]}")
            val storeMenuResponse: String = reader!!.readLine()
            Log.i("로그:서버응답", storeMenuResponse)
            //서버>앱: MAIN;STORE;storeName;waitingNum;nextName;nextNum
            if (reader != null) {
                try {
                    reader!!.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (writer != null) {
                writer!!.close()
            }
            if (clientSocket != null) {
                try {
                    clientSocket!!.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}