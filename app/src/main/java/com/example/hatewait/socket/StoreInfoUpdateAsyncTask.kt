package com.example.hatewait.socket

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.example.hatewait.StoreInfoUpdate
import com.example.hatewait.listContext
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_store_info_update.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.ref.WeakReference
import java.net.Socket

class StoreInfoUpdateAsyncTask(context : StoreInfoUpdate) : AsyncTask<Map<String, String>, Unit, String>(){
    val activityReference = WeakReference(context)
    private lateinit var clientSocket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter
    private var resultString = ""
    private lateinit var updatedStoreName : String
//앱>서버: MODIFY;STORE;가게id;가게 이름;info;영업시간;수용가능인원(anum);
// 자동호출인원수(new); 주소(new);가게 전화번호(new);pw(new)
    override fun doInBackground(vararg updatedStoreInfoMap: Map<String, String>): String {
        try {
            clientSocket = Socket(SERVERIP, PORT)
            writer = PrintWriter(clientSocket.getOutputStream(), true)
            reader = BufferedReader(InputStreamReader(clientSocket.getInputStream(), "UTF-8"))
            writer.println("MODIFY;STORE;" +
                    "${updatedStoreInfoMap[0]["ID"]};${updatedStoreInfoMap[0]["NAME"]};" +
                    "${updatedStoreInfoMap[0]["DESCRIPTION"]};${updatedStoreInfoMap[0]["BUSINESS_HOURS"]};" +
                    "${updatedStoreInfoMap[0]["CAPACITY"]};${updatedStoreInfoMap[0]["AUTO_CALL"]};" +
                    "${updatedStoreInfoMap[0]["ADDRESS"]};${updatedStoreInfoMap[0]["PHONE"]};" +
                    "${updatedStoreInfoMap[0]["PASSWORD"]}"
            )
            updatedStoreName = updatedStoreInfoMap[0]["NAME"].toString()
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

    override fun onPostExecute(result: String?) {
        val currentActivity = activityReference.get()
        if (currentActivity == null || currentActivity.isFinishing) return
        Log.i("updateResult", result)
//        임시 방편 이름 수정
        val storeReference = currentActivity.getSharedPreferences("STORE_USER_INFO", Context.MODE_PRIVATE)
        storeReference.edit().putString("STORE_NAME", updatedStoreName)
        currentActivity.update_store_info_button.isEnabled = false
        Toasty.success(
            currentActivity,
            "가게 정보 수정 완료!",
            Toast.LENGTH_SHORT,
            true
        ).show()

        super.onPostExecute(result)
    }


}