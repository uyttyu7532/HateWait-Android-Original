package com.example.hatewait.customer


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.hatewait.map.KakaoMapActivity
import com.example.hatewait.R
import com.example.hatewait.customerinfo.CustomerInfoUpdate
import com.example.hatewait.socket.CUSTOMERID
import com.example.hatewait.socket.CancelAsyncTask
import com.example.hatewait.socket.CustomerMenuAsyncTask
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_customer_menu.*
import org.jetbrains.anko.startActivity


lateinit var waitingStoreView: TextView
lateinit var customerWaitingNum: TextView
lateinit var customerNameView: TextView
lateinit var recentRefreshTime: TextView
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

        // customer mode
        val customerReference =
            getSharedPreferences(resources.getString(R.string.customer_mode), Context.MODE_PRIVATE)
        CUSTOMERID = customerReference.getString("CUSTOMER_ID", "")

        FirebaseMessaging.getInstance().subscribeToTopic(CUSTOMERID)

        Log.i("LOG_TAG", "onresume 손님메뉴, customerID=${CUSTOMERID}")
        CustomerMenuAsyncTask().execute(CUSTOMERID)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_menu)

        init()

    }


    fun init() {

        myCoupon.setOnClickListener{
            startActivity<ManageStampCouponActivity>()
//            startActivity<StoreList>()
        }

        editcustomerinfo.setOnClickListener{
            startActivity<CustomerInfoUpdate>()
        }

        otherStoreBtn.setOnClickListener {
            val intent = Intent(this, KakaoMapActivity::class.java)
            startActivity(intent)
        }

        my_cancel_button.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("정말 대기를 그만두시겠습니까?")
                .setContentText("\n")
                .setConfirmText("예")
                .setConfirmClickListener { sDialog ->
                    Log.i("로그", CUSTOMERID.toString())
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
                .setTitleText(resources.getString(R.string.notification_dialog_message))
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
//        customerMarquee.text = resources.getString(R.string.customer_marquee)
//        customerMarquee.isSelected = true // 마키 텍스트에 포커스


    }

    override fun onDestroy() {
//        메모리 누수 방지
        customView = null
        super.onDestroy()
    }


    companion object {
        private const val LOG_TAG = "CustomerMenu"
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
