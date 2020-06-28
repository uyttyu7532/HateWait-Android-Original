package com.example.hatewait


import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_name_check_dialog.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


lateinit var customerName: String
lateinit var waitingStoreName: String // 대기중인 가게 이름
lateinit var waitingMyTurn: String // 현재 대기중인 순서

lateinit var waitingStoreView: TextView
lateinit var customerWaitingNum: TextView
lateinit var customerMarquee: TextView
lateinit var customerNameView: TextView

class CustomerMenu : AppCompatActivity() {
    var customView : View? = null
    val yesButton : ImageButton by lazy {
        customView?.findViewById(R.id.name_yes_button) as ImageButton
    }
    val noButton : ImageButton by lazy {
        customView?.findViewById(R.id.name_no_button) as ImageButton
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_menu)

        customerMenuAsyncTask(this)

        fcm()

        customView = layoutInflater.inflate(R.layout.activity_name_check_dialog, null)
        // MyFirebaseMessagingService.kt > sendNotification에서 보내주는 값으로 판단
        // TODO 로그인 여부 등에 따라 코드 수정 필요
        if (intent.hasExtra("Notification")) {
            val questionDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("가게에 오실건가요??")
                .setContentText("3번째 순서 전입니다!")
//                .setConfirmText("갈게요!")
                .setCustomView(customView)
//                .setConfirmClickListener { sDialog ->
//                    sDialog.dismissWithAnimation()
//                }
//                .setCancelButton(
//                    "안가요"
//                ) { sDialog ->
//                    // 대기열에서 삭제
//                    sDialog.dismissWithAnimation()
//                }
            questionDialog.show()

//            06월 28일 추가
            yesButton.setOnClickListener {
                questionDialog.dismissWithAnimation()
            }
            noButton.setOnClickListener {
                questionDialog.dismissWithAnimation()
            }
        }

    }

    private fun fcm() {
        // 현재 토큰을 db에 저장
        //Get Firebase FCM token
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener(this,
                { instanceIdResult ->
                    Log.i("현재 토큰: ", instanceIdResult.token)
                })

        // or

        // 자기 폰번호 subscribe하면 됨!!!
//        FirebaseMessaging.getInstance().subscribeToTopic("자기 폰번호")
    }

    class customerMenuAsyncTask(context: CustomerMenu) : AsyncTask<Unit, Unit, Unit>() {

        private var clientSocket: Socket? = null
        private var reader: BufferedReader? = null // 서버 < 앱
        private var writer: PrintWriter? = null // 앱 > 서버

        private val port = 3000 // port num
        private val ip: String = R.string.serverIPAddress.toString()// 서버 ip적기
        val customerId = 12345678

        var CustomerMenuArray: Array<String>? = null

        override fun doInBackground(vararg params: Unit?) { // 소켓 연결
            try {
                clientSocket = Socket(ip, port)
                writer = PrintWriter(clientSocket!!.getOutputStream(), true)
                reader = BufferedReader(
                    InputStreamReader(
                        clientSocket!!.getInputStream(),
                        "EUC_KR"
                    )
                )
                writer!!.println("MAIN;MEMBER;customerId")
                Log.i("로그:서버에게 정보 달라고 보냄", customerId.toString())
                val storeMenuResponse: String = reader!!.readLine()
                Log.i("로그:서버응답", storeMenuResponse)
                CustomerMenuArray = storeMenuResponse.split(";").toTypedArray()
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

        override fun onPostExecute(result: Unit?) { // UI에 보이기
            super.onPostExecute(result)
            //서버>앱: MAIN;MEMBER;customerName;waitingStoreName;waitingMyTurn

            customerName = CustomerMenuArray!!.get(2)
            waitingStoreName = CustomerMenuArray!!.get(3)
            waitingMyTurn = CustomerMenuArray!!.get(4)

            customerNameView.setText(customerName)
            waitingStoreView.setText(waitingStoreName)
            customerWaitingNum.setText(waitingMyTurn)
            customerMarquee.setText("내 차례가 되면 상태바 알림과 문자 알림이 발송됩니다. 취소 버튼을 눌러 대기를 취소할 수 있습니다. ")
            customerMarquee.setSelected(true) // 마키 텍스트에 포커스

        }

    }

//        modifyCustomerInfo() // 회원 정보 수정
//        cancelWaiting() // 대기 취소하기
//        refreshMyTurn() // 내 순서 새로고침

    override fun onDestroy() {
//        메모리 누수 방지
        customView = null
        super.onDestroy()
    }
}
