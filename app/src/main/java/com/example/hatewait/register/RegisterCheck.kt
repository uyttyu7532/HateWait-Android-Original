package com.example.hatewait.register

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_register_complete.*

class RegisterCheck : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_complete)

        val customer_name: String = intent.getStringExtra("CUSTOMER_NAME")
//        val customer_phone_number : String = intent.getStringExtra("USER_PHONE_NUMBER")
        val customer_turn: Int = intent.getIntExtra("CUSTOMER_TURN", 1)
        val spannableString_greetcustomer =
            SpannableString(String.format("%s 회원님 환영합니다.", customer_name))
        spannableString_greetcustomer.setSpan(
            RelativeSizeSpan(1.5f),
            0,
            customer_name.length + 1,
            Spanned.SPAN_INTERMEDIATE
        )
        spannableString_greetcustomer.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            customer_name.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val spannableWaitingString =
            SpannableString(String.format("%d 번째 손님으로 등록되었습니다.", customer_turn))
        spannableWaitingString.setSpan(
            RelativeSizeSpan(1.5f),
            0,
            customer_turn.toString().length + 1,
            Spanned.SPAN_INTERMEDIATE
        )
        spannableWaitingString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            customer_turn.toString().length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        greeting_customer_message.text = spannableString_greetcustomer
        waiting_number_message.text = spannableWaitingString

//        5초뒤 자동으로 액티비티 종료
        Handler().postDelayed({
            finish()
        }, 5000)
    }


}