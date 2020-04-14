package com.example.hatewait

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register_complete.*

class Register_Check : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_complete)
        val customer_name : String = intent.getStringExtra("USER_NAME")

        val customer_phone_number : String = intent.getStringExtra("USER_PHONE_NUMBER")

        val spannableString_greetcustomer = SpannableString(String.format("%s 회원님 환영합니다.", customer_name))
        spannableString_greetcustomer.setSpan(RelativeSizeSpan(1.5f), 0, customer_name.length, Spanned.SPAN_INTERMEDIATE)
        spannableString_greetcustomer.setSpan(StyleSpan(Typeface.BOLD), 0, customer_name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        greeting_customer_message.setText(spannableString_greetcustomer)

    }
}