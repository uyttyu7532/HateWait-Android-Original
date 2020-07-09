package com.example.hatewait.socket

import android.os.AsyncTask
import com.example.hatewait.NonMemberRegister
import com.example.hatewait.RegisterCheck
import kotlinx.android.synthetic.main.activity_non_members_reigster.*
import org.jetbrains.anko.support.v4.startActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.ref.WeakReference
import java.net.Socket

class NonMemberRegisterAsyncTask(context: NonMemberRegister) : AsyncTask<String, Unit, String>() {
    val activityReference = WeakReference(context)
    private lateinit var clientSocket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter
    private var resultString = ""

    override fun doInBackground(vararg params: String) : String { // 소켓 연결
        val storeId= "s0000"
        val userName = params[0]
        val userPhone = params[1]
        val numOfGroup = params[2]
        try {
            clientSocket = Socket(SERVERIP, PORT)
            writer = PrintWriter(clientSocket.getOutputStream(), true)
            reader = BufferedReader(InputStreamReader(clientSocket.getInputStream(), "UTF-8"))
            writer.println("INSQUE;NONMEM;$storeId;$userName;$userPhone;$numOfGroup")
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
        if (currentActivity == null || currentActivity.isRemoving || currentActivity.isDetached) return

//            server response string : INSQUE;NONMEM;22
//            마지막 delimeter ; 이후 '22'는 대기번호를 의미
        val customerTurnNumber = result.substringAfterLast(";").toInt()
        currentActivity.startActivity<RegisterCheck>(
            "CUSTOMER_NAME" to currentActivity.user_name_input_editText.text.toString(),
            "CUSTOMER_TURN" to customerTurnNumber
        )
        super.onPostExecute(result)
    }

}