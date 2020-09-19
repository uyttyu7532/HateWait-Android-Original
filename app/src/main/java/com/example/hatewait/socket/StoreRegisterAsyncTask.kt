package com.example.hatewait.socket

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.example.hatewait.store.StoreMenu
import com.example.hatewait.storeinfo.StoreSignUp3
import org.jetbrains.anko.startActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.ref.WeakReference
import java.net.Socket

class StoreRegisterAsyncTask(context: StoreSignUp3) : AsyncTask<Map<String, String>, Unit, String>() {
    private val activityReference = WeakReference(context)
    private lateinit var clientSocket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter
    private var resultString = ""
    private lateinit var newStoreId : String
    private lateinit var newStoreName : String

    override fun doInBackground(vararg newStoreInfoMap: Map<String, String>): String {
//앱>서버: SIGNUP;STORE;가게id;가게 이름;
// info;영업시간;수 용가능인원(anum);자동호출인원수(new); 주소(new);가게 전화번호(new);pw(new)
        try {
            clientSocket = Socket(SERVERIP, PORT)
            writer = PrintWriter(clientSocket.getOutputStream(), true)
            reader = BufferedReader(InputStreamReader(clientSocket.getInputStream(), "UTF-8"))
            writer.println("SIGNUP;STORE;" +
                    "${newStoreInfoMap[0]["ID"]};${newStoreInfoMap[0]["NAME"]};" +
                    "${newStoreInfoMap[0]["DESCRIPTION"]};${newStoreInfoMap[0]["BUSINESS_HOURS"]};" +
                    "${newStoreInfoMap[0]["CAPACITY"]};${newStoreInfoMap[0]["AUTO_CALL"]};" +
                    "${newStoreInfoMap[0]["ADDRESS"]};${newStoreInfoMap[0]["PHONE"]};" +
                    "${newStoreInfoMap[0]["PASSWORD"]}"
            )
            newStoreId = newStoreInfoMap[0]["ID"].toString()
            newStoreName = newStoreInfoMap[0]["NAME"].toString()
            resultString = reader.readLine()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        } finally {
            writer.close()
            reader.close()
            clientSocket.close()
        }
        return resultString
    }

    override fun onPostExecute(result: String) {
        val currentActivity = activityReference.get()
        if (currentActivity == null || currentActivity.isFinishing) return
        Log.i("registerResult", result)
        val storeReference = currentActivity.getSharedPreferences("STORE_USER_INFO", Context.MODE_PRIVATE)
//        임시방편으로 일단 ID, 이름만 저장
        storeReference.edit().putString("STORE_ID", newStoreId).putString("STORE_NAME", newStoreName)
            .commit()
        currentActivity.startActivity<StoreMenu>()

//        remove tasks immediately below it in the current task that have the same affinity.
        currentActivity.finishAffinity()
//        currentActivity.finish()
        super.onPostExecute(result)
    }
}