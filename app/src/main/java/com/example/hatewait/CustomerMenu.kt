package com.example.hatewait


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.hatewait.socket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

lateinit var waitingStoreView: TextView
lateinit var customerWaitingNum: TextView
lateinit var customerMarquee: TextView
lateinit var customerNameView: TextView
lateinit var my_cancel_button: TextView
lateinit var my_refresh_button: ImageView
lateinit var recentRefreshTime: TextView
lateinit var waitingNumText: TextView
lateinit var customerWaiting: LinearLayout

class CustomerMenu : AppCompatActivity() {

    var customView: View? = null
    private val yesButton: ImageButton by lazy {
        customView?.findViewById<ImageButton>(R.id.name_yes_button)!!
    }
    private val noButton: ImageButton by lazy {
        customView?.findViewById<ImageButton>(R.id.name_no_button)!!
    }

    override fun onResume() {
        super.onResume()

        Log.i("로그", "onresume 손님메뉴")
        CustomerMenuAsyncTask().execute(CUSTOMERID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_menu)

        // customer mode
        val customerReference =
            getSharedPreferences(resources.getString(R.string.customer_mode), Context.MODE_PRIVATE)
        CUSTOMERID = customerReference.getString("CUSTOMER_ID", "")

        init()

    }


    fun init() {

        waitingStoreView = findViewById(R.id.waitingStoreView)
        customerWaitingNum = findViewById(R.id.customerWaitingNum)
        customerMarquee = findViewById(R.id.customerMarquee)
        customerNameView = findViewById(R.id.customerNameView)
        my_cancel_button = findViewById(R.id.my_cancel_button)
        my_refresh_button = findViewById(R.id.my_refresh_button)
        recentRefreshTime = findViewById(R.id.recentRefreshTime)
        recentRefreshTime = findViewById(R.id.recentRefreshTime)
        waitingNumText = findViewById(R.id.waitingNumText)
        customerWaiting = findViewById(R.id.customerWaiting)

        my_cancel_button.setOnClickListener {
            // storeid, customerid
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("정말 대기를 그만두시겠습니까?")
                .setContentText("\n")
                .setConfirmText("예")
                .setConfirmClickListener { sDialog ->
                    CancelAsyncTask().execute(CUSTOMERID)
                    CustomerMenuAsyncTask().execute(CUSTOMERID)
                    sDialog.dismissWithAnimation()
                }
                .setCancelButton(
                    "아니오"
                ) { sDialog -> sDialog.dismissWithAnimation() }
                .show()
        }

        my_refresh_button.setOnClickListener {
            CustomerMenuAsyncTask().execute(CUSTOMERID)
        }


        customView = layoutInflater.inflate(R.layout.activity_name_check_dialog, null)
        if (intent.hasExtra("Notification")) {
            val questionDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("가게에 오셔서 직원에게 안내를 받으세요. 가게에 오실 건가요?")
                .hideConfirmButton()
                .setCustomView(customView)
            questionDialog.show()

            yesButton.setOnClickListener {
                questionDialog.dismissWithAnimation()
            }
            noButton.setOnClickListener {
                CancelAsyncTask().execute(CUSTOMERID)
                questionDialog.dismissWithAnimation()
            }
        }
        customerMarquee.text = "내 차례가 되면 상태바 알림과 문자 알림이 발송됩니다. 취소 버튼을 눌러 대기를 취소할 수 있습니다."
        customerMarquee.isSelected = true // 마키 텍스트에 포커스


    }

    override fun onDestroy() {
//        메모리 누수 방지
        customView = null
        super.onDestroy()
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
