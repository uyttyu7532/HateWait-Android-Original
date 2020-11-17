package com.example.hatewait.member

import LottieDialogFragment.Companion.fragment
import LottieDialogFragment.Companion.newInstance
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.hatewait.map.KakaoMapActivity
import com.example.hatewait.R
import com.example.hatewait.login.LoginInfo.memberInfo
import com.example.hatewait.memberinfo.MemberInfoUpdate
import com.example.hatewait.model.MyWaitingResponseData
import com.example.hatewait.retrofit2.MyApi
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_customer_menu.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MemberMenu : AppCompatActivity() {

    lateinit var mContext: Context
    lateinit var recentRefreshTime: TextView

    var customView: View? = null
    private val yesButton: ImageButton by lazy {
        customView?.findViewById<ImageButton>(R.id.name_yes_button)!!
    }
    private val noButton: ImageButton by lazy {
        customView?.findViewById<ImageButton>(R.id.name_no_button)!!
    }

    override fun onResume() {
        super.onResume()

//        val customerReference =
//            getSharedPreferences(resources.getString(R.string.customer_mode), Context.MODE_PRIVATE)
//        CUSTOMERID = customerReference.getString("CUSTOMER_ID", "")

        FirebaseMessaging.getInstance().subscribeToTopic("0" + memberInfo.phone)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_menu)

        mContext = this
        recentRefreshTime = findViewById(R.id.recentRefreshTime)


        init()
        customer_name_text_view.text = memberInfo?.name
        getMyWaiting()
    }


    fun init() {


        myCoupon.setOnClickListener {
            startActivity<StoreList>()
        }

        editcustomerinfo.setOnClickListener {
            startActivity<MemberInfoUpdate>()
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
                    // TODO 대기취소
                    sDialog.dismissWithAnimation()
                }
                .setCancelButton(
                    "아니오"
                ) { sDialog -> sDialog.dismissWithAnimation() }
                .show()
        }

        my_refresh_button.setOnClickListener {
            getMyWaiting()
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
// TODO 대기취소
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

    private fun getMyWaiting() {
        if (fragment == null || (!(fragment?.isAdded)!!)) {
            newInstance().show(supportFragmentManager, "")
        }
        MyApi.WaitingListService.requestMyWaiting(memberInfo!!.id)
            .enqueue(object : Callback<MyWaitingResponseData> {
                override fun onFailure(call: Call<MyWaitingResponseData>, t: Throwable) {

                    Log.d("retrofit2 대기현황 조회 :: ", "서버 연결 실패 $t")
                }

                override fun onResponse(
                    call: Call<MyWaitingResponseData>,
                    response: Response<MyWaitingResponseData>
                ) {
                    newInstance().dismiss()
                    Log.d(
                        "retrofit2 대기현황 조회 ::",
                        response.code().toString() + response.body().toString()
                    )
                    when (response.code()) {
                        200 -> {
                            refreshTime(recentRefreshTime)
                            val data = response.body() // 서버로부터 온 응답
                            if (data?.message.equals("대기중인 가게가 없습니다!")) {
                                no_waiting_text_view.visibility = VISIBLE
                                customer_waiting_linear_layout.visibility = GONE
                                waiting_store_text_view.visibility = GONE
                            } else {
                                no_waiting_text_view.visibility = GONE
                                customer_waiting_linear_layout.visibility = VISIBLE
                                waiting_store_text_view.text = data!!.store_name
                                customer_waiting_num_text_view.text = data!!.turn_number.toString()+"번 순서입니다"
                            }
                        }
                    }

                }
            }
            )
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

fun refreshTime(text_view: TextView) {
    var reservationTime = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
    val curTime = dateFormat.format(Date(reservationTime))
    text_view.text = "최근 새로고침 시간: ${curTime}"
}
