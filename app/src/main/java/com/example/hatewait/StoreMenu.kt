package com.example.hatewait

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_store_menu.*
import org.jetbrains.anko.startActivity

class StoreMenu : AppCompatActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_menu)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

//        imageButton2 이름 바꿀 필요성 있음 ex) tablet_mode_button
        imageButton2.setOnClickListener {
            startActivity<LoginRegisterViewPagerActivity>(
            )
        }

        var nextname = "조예린"
        var namenum = 3
        nextText.setText(String.format("다음 순서 : %s 외 %d명 입니다. 호출 버튼을 눌러 다음 대기자에게 알림을 보내주세요", nextname, namenum));


        var storename= intent.getStringExtra("storename")
        var waitingnum = intent.getStringExtra("waitingnum")




        val spannableString_greetstore = SpannableString(String.format("%s 회원님 환영합니다.", storename))
        spannableString_greetstore.setSpan(RelativeSizeSpan(1.5f), 0, storename.length, Spanned.SPAN_INTERMEDIATE)
        spannableString_greetstore.setSpan(StyleSpan(Typeface.BOLD), 0, storename.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannableString_greetstore.setSpan(ForegroundColorSpan(Color.parseColor("#000000")), 0, storename.length, Spanned.SPAN_INTERMEDIATE)
        greetStoreText.setText(spannableString_greetstore)

        val spannableString_waitingnum = SpannableString(String.format("%s 명 대기중 입니다.",waitingnum))
        spannableString_waitingnum.setSpan(RelativeSizeSpan(3.0f), 0, waitingnum.length, Spanned.SPAN_INTERMEDIATE)
        spannableString_waitingnum.setSpan(StyleSpan(Typeface.BOLD), 0, waitingnum.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannableString_waitingnum.setSpan(ForegroundColorSpan(Color.parseColor("#000000")), 0, waitingnum.length, Spanned.SPAN_INTERMEDIATE)
        waitingnumText.setText(spannableString_waitingnum)

            nextText.setSelected(true) // 마키 텍스트에 포커스


    }



}