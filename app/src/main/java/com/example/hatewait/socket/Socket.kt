package com.example.hatewait.socket

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.example.hatewait.*
import com.example.hatewait.model.newClient
import es.dmoral.toasty.Toasty
import hatewait.vo.QueueListSerializable
import java.io.*
import java.net.Socket
import java.nio.charset.StandardCharsets

val SERVERIP: String = "192.168.56.1"
val PORT = 3000// port num
var STOREID = "s0000"

var clientSocket: Socket? = null
var reader: BufferedReader? = null // 서버 < 앱
var writer: PrintWriter? = null // 앱 > 서버


class storeMenuAsyncTask : AsyncTask<Unit, Unit, Array<String>?>() {

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
        try {
            super.onPostExecute(result)
            Log.i("로그", " storeMenuAsyncTask-onPostExecute:: ok")
            //서버>앱: MAIN;STORE;storeName;waitingNum;nextName;nextNum

            storeNameView.setText(result?.get(2))
            storeWaitingNum.setText(result?.get(3))
            storeMarquee.setText(
                String.format(
                    "다음 순서 : %s 외 %d명 입니다. 호출 버튼을 눌러 다음 대기자에게 알림을 보내주세요. 다음 순서 : %s 외 %d명 입니다. 호출 버튼을 눌러 다음 대기자에게 알림을 보내주세요",
                    result?.get(4),
                    result?.get(5)?.toInt(),
                    result?.get(4),
                    result?.get(5)?.toInt()
                )
            )
            storeMarquee.setSelected(true) // 마키 텍스트에 포커스
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("로그", " storeMenuAsyncTask-onPostExecute:: ${e}")
        }
    }
}


class addCustomerTask : AsyncTask<newClient, Unit, Unit>() {

    override fun doInBackground(vararg params: newClient) { // 소켓 연결
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

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        Toasty.success(listContext, "추가되었습니다", Toast.LENGTH_SHORT, true).show()
        setRecyclerView()
    }

}

class storeWaitingListAsyncTask : AsyncTask<Unit, Unit, QueueListSerializable?>() {

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
        super.onPostExecute(result)
        Log.i("로그", "storeWaitingListAsyncTask - onPostExecute :: ok")
        Log.i("로그", "result ::" + result.toString() ?: "전달된 리스트가 없습니다.")

        autoCallSwitchView.text = "${result?.autonum}번째 팀까지 자동호출"

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
        setRecyclerView()
    }
}

class customerMenuAsyncTask : AsyncTask<Unit, Unit, Array<String>?>() {

    var CustomerMenuArray: Array<String>? = null

    override fun doInBackground(vararg params: Unit?): Array<String>? { // 소켓 연결
        try {
            clientSocket = Socket(SERVERIP, PORT)
            Log.i("로그", " customerMenuAsyncTask:: ok")
            writer = PrintWriter(clientSocket!!.getOutputStream(), true)
            reader = BufferedReader(
                InputStreamReader(
                    clientSocket!!.getInputStream(),
                    StandardCharsets.UTF_8
                )
            )
            writer!!.println("MAIN;MEMBER;m2222")
            val storeMenuResponse: String = reader!!.readLine()
            Log.i("로그", "서버응답: " + storeMenuResponse)
            CustomerMenuArray = storeMenuResponse.split(";").toTypedArray()
            Log.i("로그", "CustomerMenu > Server::" + CustomerMenuArray.toString())
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
            Log.i("로그", " customerMenuAsyncTask: ${e}")
        }
        return CustomerMenuArray
    }

    override fun onPostExecute(result: Array<String>?) { // UI에 보이기
        super.onPostExecute(result)
        Log.i("로그", " customerMenuAsyncTask-onPostExecute:: ok")
        //서버>앱: MAIN;MEMBER;customerName;waitingStoreName;waitingMyTurn

        customerNameView.setText(result?.get(2) ?: "손님이름")
        waitingStoreView.setText(result?.get(3) ?: "대기중인 가게가 없습니다.")
        customerWaitingNum.setText(result?.get(4) ?: "0")
        customerMarquee.setText("내 차례가 되면 상태바 알림과 문자 알림이 발송됩니다. 취소 버튼을 눌러 대기를 취소할 수 있습니다. ")
        customerMarquee.setSelected(true) // 마키 텍스트에 포커스
    }
}