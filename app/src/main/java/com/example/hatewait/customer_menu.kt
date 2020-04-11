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

class customer_menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_menu)

        var customername= intent.getStringExtra("customername")




        val spannableString_greetcustomer = SpannableString(String.format("%s 회원님 환영합니다.", customername))
        spannableString_greetcustomer.setSpan(RelativeSizeSpan(1.5f), 0, customername.length, Spanned.SPAN_INTERMEDIATE)
        spannableString_greetcustomer.setSpan(StyleSpan(Typeface.BOLD), 0, customername.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannableString_greetstore.setSpan(ForegroundColorSpan(Color.parseColor("#000000")), 0, storename.length, Spanned.SPAN_INTERMEDIATE)
        greetCustomerText.setText(spannableString_greetcustomer)

        marquee.setSelected(true) // 마키 텍스트에 포커스
    }
}
