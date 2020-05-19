package com.example.hatewait

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_customer_menu.*

class CustomerMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_menu)

        var customerName = intent.getStringExtra("customername")
//        customerName:String // 로그인 한 손님 이름
//        waitingStoreName:String // 대기중인 가게 이름
//        waitingMyTurn:Int // 현재 대기중인 순서


        fun setMenu() {
            val spannableString_greetcustomer =
                SpannableString(String.format("%s 회원님 환영합니다.", customerName))
            spannableString_greetcustomer.setSpan(
                RelativeSizeSpan(1.5f),
                0,
                customerName.length,
                Spanned.SPAN_INTERMEDIATE
            )
            spannableString_greetcustomer.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                customerName.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
//        spannableString_greetstore.setSpan(ForegroundColorSpan(Color.parseColor("#000000")), 0, storename.length, Spanned.SPAN_INTERMEDIATE)
            greetCustomerText.setText(spannableString_greetcustomer)
            marquee.setSelected(true) // 마키 텍스트에 포커스
        }

//        modifyCustomerInfo() // 회원 정보 수정
////        cancelWaiting() // 대기 취소하기
////        refreshMyTurn() // 내 순서 새로고침


    }
}
