package com.example.hatewait

import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.store_menu.*
import java.io.IOException



class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_menu)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//
//        button_store.setOnClickListener{
//            startActivity<StoreMenu_grid>()
//                "id" to idView.text.toString()
//
//        }
//
//        button_customer.setOnClickListener{
//            startActivity<CustomerMenu>()
//        }
//
//
//
//        //현재 기기의 토큰을 가져와서 출력 해보자.
//        myToken()
//

        var nextname = "조예린"
        var namenum = 3
        nextText.setText(String.format("다음 순서 : %s 외 %d명 입니다. 호출 버튼을 눌러 다음 대기자에게 알림을 보내주세요", nextname, namenum));


        var storename = "건대떡볶이"
        var waitingnum = "100"

        val spannableString_greetstore = SpannableString(String.format("%s 회원님 환영합니다.",storename))
        spannableString_greetstore.setSpan(RelativeSizeSpan(1.5f), 0, storename.length, Spanned.SPAN_INTERMEDIATE)
        spannableString_greetstore.setSpan(StyleSpan(Typeface.BOLD), 0, storename.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannableString_greetstore.setSpan(ForegroundColorSpan(Color.parseColor("#000000")), 0, storename.length, Spanned.SPAN_INTERMEDIATE)
        greetStoreText.setText(spannableString_greetstore)

        val spannableString_waitingnum = SpannableString(String.format("%s 명 대기중 입니다.",waitingnum))
        spannableString_waitingnum.setSpan(RelativeSizeSpan(3.0f), 0, waitingnum.length, Spanned.SPAN_INTERMEDIATE)
        spannableString_waitingnum.setSpan(StyleSpan(Typeface.BOLD), 0, waitingnum.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannableString_waitingnum.setSpan(ForegroundColorSpan(Color.parseColor("#000000")), 0, waitingnum.length, Spanned.SPAN_INTERMEDIATE)
        waitingnumText.setText(spannableString_waitingnum)

//        text.setSpan(ForegroundColorSpan([색상 코드]), [시작 위치], [끝 위치], Spanned.SPAN_INTERMEDIATE)
//        text.setSpan(ForegroundColorSpan(Color.parseColor("#5F00FF")), 0, waitingnum.length, Spanned.SPAN_INTERMEDIATE)


        nextText.setSelected(true)




    }

    private fun myToken() =//쓰레드 사용할것
        Thread(Runnable {
            try {
                FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.i(TAG, "getInstanceId failed", task.exception)
                            return@OnCompleteListener
                        }
                        val token = task.result?.token
//                        textView.text = token
                        //Log.d(TAG, token)
                    })
            } catch (e: IOException){
                e.printStackTrace()
            }
        }).start()




}