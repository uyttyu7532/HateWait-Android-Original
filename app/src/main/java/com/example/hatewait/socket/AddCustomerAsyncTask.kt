package com.example.hatewait.socket

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.example.hatewait.ClientData
import com.example.hatewait.clientList
import com.example.hatewait.listContext
import com.example.hatewait.model.newClient
import com.example.hatewait.setRecyclerView
import es.dmoral.toasty.Toasty
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets

class AddCustomerAsyncTask : AsyncTask<newClient, Unit, String?>() {

    var tempClientName = ""

    override fun doInBackground(vararg params: newClient): String? { // 소켓 연결
        try {
            clientSocket = Socket(SERVERIP, PORT)
            Log.i("로그", "addCustomerTask:: ok")
            writer = PrintWriter(clientSocket!!.getOutputStream(), true)
            reader = BufferedReader(
                InputStreamReader(
                    clientSocket!!.getInputStream(),
                    StandardCharsets.UTF_8
                )
            )

            tempClientName = params[0].name

            //TODO INSQUE;NONMEM;가게id;이름;전화번호;대기인원
            writer!!.println("INSQUE;NONMEM;${STOREID};" + "${params[0].name};${params[0].phoneNum};${params[0].peopleNum}")
            Log.i(
                "로그: 대기 추가 서버로 보냄",
                "INSQUE;NONMEM;${STOREID};" + "${params[0].name};${params[0].phoneNum};${params[0].peopleNum}"
            )
            val addCustomerResponse: String = reader!!.readLine()
            Log.i("로그: add서버응답", addCustomerResponse)

            var data_tmp =
                ClientData(
                    "",
                    params[0].phoneNum,
                    params[0].name,
                    params[0].peopleNum,
                    ""
                )
            clientList?.add(data_tmp)

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
            return addCustomerResponse

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPostExecute(result: String?) {
        try {
            super.onPostExecute(result)
            if (result != null) {
                Toasty.success(
                    listContext,
                    tempClientName + " 손님 추가 완료",
                    Toast.LENGTH_SHORT,
                    true
                ).show()
                tempClientName=""
                setRecyclerView()
            }else{
                Toasty.normal(
                    listContext,
                    "서버 연결을 확인해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("로그", " addCustomerTask-onPostExecute:: ${e}")
        }
    }

}