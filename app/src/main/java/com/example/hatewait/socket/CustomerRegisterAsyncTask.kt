package com.example.hatewait.socket

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.example.hatewait.CustomerMenu
import com.example.hatewait.CustomerRegister2
import com.example.hatewait.R
import com.google.firebase.messaging.FirebaseMessaging
import org.jetbrains.anko.startActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.ref.WeakReference
import java.net.Socket

class CustomerRegisterAsyncTask(context: CustomerRegister2) : AsyncTask<String, Unit, Unit>() {
    private val activityReference = WeakReference(context)
    private lateinit var clientSocket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter
    private lateinit var newCustomerId : String

    override fun doInBackground(vararg params: String) { // 소켓 연결
        val userId= params[0]
        val userPassword = params[1]
        val userName = params[2]
        val userPhone = params[3]
        try {
            clientSocket = Socket(SERVERIP, PORT)
            writer = PrintWriter(clientSocket!!.getOutputStream(), true)
            reader = BufferedReader(InputStreamReader(clientSocket!!.getInputStream(), "UTF-8"))
            writer!!.println("SIGNUP;MEMBER;$userId;$userName;$userPhone;$userPassword")
            newCustomerId = userId
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        } finally {
            reader.close()
            writer.close()
            clientSocket.close()
        }
        Log.i("로그","subscribeToTopic(${userPhone})")
        FirebaseMessaging.getInstance().subscribeToTopic(userPhone)
    }

    override fun onPostExecute(result: Unit?) {
        val currentActivity = activityReference.get()
        if (currentActivity == null || currentActivity.isFinishing) return
        val customerReference = currentActivity.getSharedPreferences(currentActivity.resources.getString(R.string.customer_mode), Context.MODE_PRIVATE)
        customerReference.edit().putString("CUSTOMER_ID", newCustomerId).commit()
        currentActivity.startActivity<CustomerMenu>()
        currentActivity.finishAffinity()
        super.onPostExecute(result)
    }

}