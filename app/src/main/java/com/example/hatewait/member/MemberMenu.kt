package com.example.hatewait.member


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.hatewait.map.KakaoMapActivity
import com.example.hatewait.R
import com.example.hatewait.memberinfo.CustomerInfoUpdate
import com.example.hatewait.login.memberInfo
import com.example.hatewait.socket.CUSTOMERID
import com.example.hatewait.socket.CancelAsyncTask
import kotlinx.android.synthetic.main.activity_customer_menu.*
import org.jetbrains.anko.startActivity


//lateinit var waitingStoreView: TextView
//lateinit var customerWaitingNum: TextView
//lateinit var customerNameTextView: TextView
//lateinit var recentRefreshTime: TextView
//lateinit var customerWaiting: LinearLayout

class MemberMenu : AppCompatActivity() {

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
//            getSharedPreferences(resources.getString(R.string.customer_mode), Context.MODE_PRIVATE)
//        CUSTOMERID = customerReference.getString("CUSTOMER_ID", "")

            //TODO sub topic
//        FirebaseMessaging.getInstance().subscribeToTopic(CUSTOMERID)

        Log.i("LOG_TAG", "onresume 손님메뉴, customerID=${CUSTOMERID}")
//        CustomerMenuAsyncTask().execute(CUSTOMERID)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_menu)

        init()
        retrofit2()

    }


    fun init() {


        myCoupon.setOnClickListener {
//            startActivity<ManageStampCouponActivity>()
            startActivity<StoreList>()
        }

        editcustomerinfo.setOnClickListener {
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
//                    CustomerMenuAsyncTask().execute(CUSTOMERID)
                    sDialog.dismissWithAnimation()
                }
                .setCancelButton(
                    "아니오"
                ) { sDialog -> sDialog.dismissWithAnimation() }
                .show()
        }

        my_refresh_button.setOnClickListener {
//            CustomerMenuAsyncTask().execute(CUSTOMERID)
        }

//        customerNameTextView = customView!!.findViewById(R.id.customer_name_text_view)

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



    private fun retrofit2() {
//        if (CustomerInfo == "null") {
//            com.example.hatewait.customer.waitingStoreView.text = "대기중인 가게가 없습니다."
//            com.example.hatewait.customer.customerWaiting.visibility = View.GONE
//        } else {
        customer_name_text_view.text = memberInfo?.name
//            com.example.hatewait.customer.waitingStoreView.text = result?.get(3)
//            com.example.hatewait.customer.customerWaitingNum.text = result?.get(4)
//            com.example.hatewait.customer.customerWaiting.visibility = View.VISIBLE
//        }

//        var reservationTime = System.currentTimeMillis() + 1000 * 60 * 60 * 9
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
//        val curTime = dateFormat.format(Date(reservationTime))
//        com.example.hatewait.customer.recentRefreshTime.text = "최근 새로고침 시간: ${curTime}"
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

