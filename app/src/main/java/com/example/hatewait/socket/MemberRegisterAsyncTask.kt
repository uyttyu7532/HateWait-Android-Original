package com.example.hatewait.socket

import android.os.AsyncTask
import android.util.Log
import com.example.hatewait.register.MemberRegister
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.ref.WeakReference
import java.net.Socket

class MemberRegisterAsyncTask(context: MemberRegister) : AsyncTask<String, Unit, String>() {
    private val activityReference = WeakReference(context)
    private lateinit var clientSocket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter
    private var responseString = ""

    override fun doInBackground(vararg params: String): String { // 소켓 연결
        val storeId= STOREID
        val userId = params[0]
        val numOfGroup = params[1]

        try {
            clientSocket = Socket(SERVERIP, PORT)
            reader = BufferedReader(InputStreamReader(clientSocket.getInputStream(), "UTF-8"))
            writer = PrintWriter(clientSocket.getOutputStream(), true)
            writer.println("INSQUE;MEMBER;$storeId;$userId;$numOfGroup")
            Log.i("request","INSQUE;MEMBER;$storeId;$userId;$numOfGroup")
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }

        try {
            responseString = reader.readLine()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        } finally {
            writer.close()
            reader.close()
            clientSocket.close()
        }
        Log.i("responseString", responseString)
        return responseString
    }

    override fun onPostExecute(result: String) {
        val currentActivity = activityReference.get()
        val memberInfoArray = result.split(";")
//            Memory Leak 방지
        if (currentActivity == null || currentActivity.isRemoving || currentActivity.isDetached) return
        Log.i("responseArray", "${memberInfoArray[0]}, ${memberInfoArray[1]}")
//            ERROR 앞에 이상한 기호가 붙기 때문에 contains 메소드 사용
        if (memberInfoArray[0].contains("ERROR") && memberInfoArray[1] == "NOTEXIST") {
            currentActivity.showMemberIdErrorDialog()
        } else {
            currentActivity.customerName = memberInfoArray[2]
            currentActivity.customerTurn = memberInfoArray[3].toInt()
            Log.i("response", "${memberInfoArray[2]} , ${memberInfoArray[3]}")
            currentActivity.customerInfoListener.registerCustomer(currentActivity)
            currentActivity.showNameCheckDialog()
        }
        super.onPostExecute(result)
    }

}