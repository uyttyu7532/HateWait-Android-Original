package com.example.hatewait

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_register_complete.*

class RegisterCheck : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_complete)

//        회원으로 부터 온건지, 비회원으로부터 온건지 체크할 수 있는 방법을 찾아봐야함
//        argument로 boolean Type flag를 넘기는 방법도 있지만 비효율적,
//        분명 intent에 관련된 다른 메소드가 있을거라고 예상.
//        val customer_id : String = intent.getStringExtra("USER_ID")
        val customer_name : String = intent.getStringExtra("CUSTOMER_NAME")
//        val customer_phone_number : String = intent.getStringExtra("USER_PHONE_NUMBER")
        val customer_turn : Int = intent.getIntExtra("CUSTOMER_TURN", 1)
        val spannableString_greetcustomer = SpannableString(String.format("%s 회원님 환영합니다.", customer_name))
        spannableString_greetcustomer.setSpan(RelativeSizeSpan(1.5f), 0, customer_name.length + 1, Spanned.SPAN_INTERMEDIATE)
        spannableString_greetcustomer.setSpan(StyleSpan(Typeface.BOLD), 0, customer_name.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val spannableWaitingString = SpannableString(String.format("%d 번째 손님으로 등록되었습니다.", customer_turn))
        spannableString_greetcustomer.setSpan(RelativeSizeSpan(1.5f), 0, customer_turn.toString().length + 1, Spanned.SPAN_INTERMEDIATE)
        spannableString_greetcustomer.setSpan(StyleSpan(Typeface.BOLD), 0, customer_turn.toString().length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        greeting_customer_message.text = spannableString_greetcustomer
        waiting_number_message.text = spannableWaitingString

    }


}