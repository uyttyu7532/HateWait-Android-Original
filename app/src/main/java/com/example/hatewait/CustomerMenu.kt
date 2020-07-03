package com.example.hatewait


import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.hatewait.socket.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets

// 앱;서버: MAIN;MEMBER;손님(회원) id
// 서버;앱: MAIN;MEMBER;이름;대기중인 가게이름;내순서

lateinit var customerId: String // 손님 id (app > server)
lateinit var customerName: String // 손님이름 (server > app)
lateinit var waitingStoreName: String // 대기중인 가게 이름 (server > app)
lateinit var waitingMyTurn: String // 현재 대기중인 순서 (server > app)

lateinit var waitingStoreView: TextView
lateinit var customerWaitingNum: TextView
lateinit var customerMarquee: TextView
lateinit var customerNameView: TextView
lateinit var my_cancel_button: TextView
lateinit var my_refresh_button: ImageView

class CustomerMenu : AppCompatActivity() {

    var customView: View? = null

    val yesButton: ImageButton by lazy {
        customView?.findViewById(R.id.name_yes_button) as ImageButton
    }
    val noButton: ImageButton by lazy {
        customView?.findViewById(R.id.name_no_button) as ImageButton
    }

    override fun onResume() {
        super.onResume()
        customerMenuAsyncTask().execute()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_menu)

        customerId = "customerId"

        init()

    }



    fun init() {

        waitingStoreView = findViewById(R.id.waitingStoreView) as TextView
        customerWaitingNum = findViewById(R.id.customerWaitingNum) as TextView
        customerMarquee = findViewById(R.id.customerMarquee) as TextView
        customerNameView = findViewById(R.id.customerNameView) as TextView
        my_cancel_button = findViewById(R.id.my_cancel_button) as TextView
        my_refresh_button = findViewById(R.id.my_refresh_button) as ImageView

        my_cancel_button.setOnClickListener {
            // storeid, customerid
            delCustomerTask().execute()
        }

        my_refresh_button.setOnClickListener {
            // 내 id
            customerMenuAsyncTask().execute()
        }


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
                delCustomerTask().execute()
                questionDialog.dismissWithAnimation()
            }
        }
    }


//    private fun fcm() {
//        // 현재 토큰을 db에 저장
//        //Get Firebase FCM token
//        FirebaseInstanceId.getInstance().instanceId
//            .addOnSuccessListener(this,
//                { instanceIdResult ->
//                    Log.i("현재 토큰: ", instanceIdResult.token)
//                })
//
//        // 회원가입 or 회원 정보 수정시 본인 폰번호 subscribe하기.
//        FirebaseMessaging.getInstance().subscribeToTopic("${customerPhoneNum}")
//    }

    class customerMenuAsyncTask : AsyncTask<Unit, Unit, Array<String>?>() {

        var CustomerMenuArray: Array<String>? = null

        override fun doInBackground(vararg params: Unit?):Array<String>? { // 소켓 연결
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
                writer!!.println("MAIN;MEMBER;m0001")
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
            }
            return CustomerMenuArray
        }

        override fun onPostExecute(result: Array<String>?) { // UI에 보이기
            super.onPostExecute(result)
            Log.i("로그", " customerMenuAsyncTask-onPostExecute:: ok")
            //서버>앱: MAIN;MEMBER;customerName;waitingStoreName;waitingMyTurn

            customerNameView.setText(result?.get(2) ?: "손님이름")
            waitingStoreView.setText(result?.get(3) ?: "대기중인 가게가 없습니다.")
            customerWaitingNum.setText(result?.get(4)?: "0")
            customerMarquee.setText("내 차례가 되면 상태바 알림과 문자 알림이 발송됩니다. 취소 버튼을 눌러 대기를 취소할 수 있습니다. ")
            customerMarquee.setSelected(true) // 마키 텍스트에 포커스
        }
    }

    override fun onDestroy() {
//        메모리 누수 방지
        customView = null
        super.onDestroy()
    }
}
