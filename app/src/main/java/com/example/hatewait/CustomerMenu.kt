package com.example.hatewait


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.hatewait.socket.*
import java.text.SimpleDateFormat
import java.util.*

lateinit var waitingStoreView: TextView
lateinit var customerWaitingNum: TextView
lateinit var customerMarquee: TextView
lateinit var customerNameView: TextView
lateinit var my_cancel_button: TextView
lateinit var my_refresh_button: ImageView
lateinit var recentRefreshTime: TextView

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
        var reservationTime = System.currentTimeMillis() + 1000*60*60*9
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val curTime = dateFormat.format(Date(reservationTime))
        recentRefreshTime.text = "최근 새로고침 시간: ${curTime}"
        Log.i("로그","onresume 손님메뉴")
        CustomerMenuAsyncTask().execute()
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

        waitingStoreView = findViewById<TextView>(R.id.waitingStoreView)
        customerWaitingNum = findViewById<TextView>(R.id.customerWaitingNum)
        customerMarquee = findViewById<TextView>(R.id.customerMarquee)
        customerNameView = findViewById<TextView>(R.id.customerNameView)
        my_cancel_button = findViewById<TextView>(R.id.my_cancel_button)
        my_refresh_button = findViewById<ImageView>(R.id.my_refresh_button)
        recentRefreshTime = findViewById<TextView>(R.id.recentRefreshTime)

        my_cancel_button.setOnClickListener {
            // storeid, customerid
            DelCustomerAsyncTask().execute()
        }

        my_refresh_button.setOnClickListener {
            // 내 id
            CustomerMenuAsyncTask().execute()
        }


        customView = layoutInflater.inflate(R.layout.activity_name_check_dialog, null)
        if (intent.hasExtra("Notification")) {
            val questionDialog = SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("가게에 오셔서 직원에게 안내를 받으세요. 가게에 오실 건가요?")
                .hideConfirmButton()
                .setCustomView(customView)
            questionDialog.show()

            yesButton.setOnClickListener {
                questionDialog.dismissWithAnimation()
            }
            noButton.setOnClickListener {
                CancelAsyncTask().execute()
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


    override fun onDestroy() {
//        메모리 누수 방지
        customView = null
        super.onDestroy()
    }
}
