package com.example.hatewait.socket

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets

class delCustomerTask : AsyncTask<String, Unit, Unit>() {

    override fun doInBackground(vararg params: String) { // 소켓 연결
        try {
            clientSocket = Socket(SERVERIP, PORT)
            Log.i("로그", "delCustomerTask:: ok")
            writer = PrintWriter(clientSocket!!.getOutputStream(), true)
            reader = BufferedReader(
                InputStreamReader(
                    clientSocket!!.getInputStream(),
                    StandardCharsets.UTF_8
                )
            )
            // TODO 삭제할 명단 :::: DELQUE;가게 id;손님id
            Log.i("로그: 대기열 삭제 요청", "DELQUE;${STOREID};${params[0]}")
            writer!!.println("DELQUE;${STOREID};${params[0]}")

            val addCustomerResponse: String = reader!!.readLine()
            Log.i("로그: del서버응답", addCustomerResponse)


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
}