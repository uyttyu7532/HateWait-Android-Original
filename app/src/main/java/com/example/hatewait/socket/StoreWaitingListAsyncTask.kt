package com.example.hatewait.socket

import android.os.AsyncTask
import android.util.Log
import com.example.hatewait.*
import hatewait.vo.QueueListSerializable
import java.io.IOException
import java.io.ObjectInputStream
import java.io.PrintWriter
import java.net.Socket

class StoreWaitingListAsyncTask : AsyncTask<Unit, Unit, QueueListSerializable?>() {

    var qls: QueueListSerializable? = null

    override fun doInBackground(vararg params: Unit?): QueueListSerializable? { // 소켓 연결

        try {
            clientSocket = Socket(SERVERIP, PORT)
            Log.i("로그", "storeWaitingListAsyncTask:: ok")
            writer = PrintWriter(clientSocket!!.getOutputStream(), true)
            writer!!.println("STRQUE;${STOREID}")

            val ois = ObjectInputStream(clientSocket!!.getInputStream()) //객체 받는 스트림
            qls = ois.readObject() as QueueListSerializable

            if (ois != null) {
                try {
                    ois!!.close()
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
        } catch (e: Exception) {
            Log.i("로그", e.toString())
            println(e)
        }

        return qls
    }

    override fun onPostExecute(result: QueueListSerializable?) { // UI에 보이기
        if (result != null) {
            try {
                super.onPostExecute(result)
                Log.i("로그", "storeWaitingListAsyncTask - onPostExecute :: ok")
                Log.i("로그", "result ::" + result.toString() ?: "전달된 리스트가 없습니다.")

                AUTONUM = result?.autonum
//        autoCallSwitchView.text = "${result?.autonum}번째 팀까지 자동호출"

                clientList.clear()

                for (i in 0..(result?.qivo?.size?.minus(1) ?: 0)) {
                    var data_tmp =
                        ClientData(
                            result?.qivo?.get(i)?.id.toString(),
                            result?.qivo?.get(i)?.phone.toString(),
                            result?.qivo?.get(i)?.name.toString(),
                            result?.qivo?.get(i)?.peopleNum.toString(),
                            result?.qivo?.get(i)?.turn.toString()
                        )
                    clientList?.add(data_tmp)
                }

//        if (AUTOCALL!!) {
//            var clientCallList: MutableList<ClientData>? = null
//            for (i in clientList) {
//                if (i.turn.toInt() <= AUTONUM) {
//                    clientCallList?.add(i)
//                }
//            }
//            autocall(clientCallList)
//        }
                setRecyclerView()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("로그", " storeWaitingListAsyncTask-onPostExecute:: ${e}")
            }
        } else {

        }
    }
}