package com.example.hatewait

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_members_register.*
import org.jetbrains.anko.startActivity
import java.net.PasswordAuthentication


class MainActivity : AppCompatActivity() {
    private val idRegex = Regex("^(?=.*[a-zA-Zㄱ-ㅎ가-힣0-9])[a-zA-Zㄱ-ㅎ가-힣0-9]{1,}$")
    private val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")
    fun verifyId(input_id : String) : Boolean = idRegex.matches(input_id)
    fun verifyPassword(input_password : String) : Boolean = passwordRegex.matches(input_password)

    private val TAG = "MainActivity"
    @SuppressLint("SourceLockedOrientationActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        init()
        addTextChangeListener()
    }


//        //현재 기기의 토큰을 가져와서 출력 해보자.
//        myToken()
//    private fun myToken() =//쓰레드 사용할것
//        Thread(Runnable {
//            try {
//                FirebaseInstanceId.getInstance().instanceId
//                    .addOnCompleteListener(OnCompleteListener { task ->
//                        if (!task.isSuccessful) {
//                            Log.i(TAG, "getInstanceId failed", task.exception)
//                            return@OnCompleteListener
//                        }
//                        val token = task.result?.token
////                        textView.text = token
//                        //Log.d(TAG, token)
//                    })
//            } catch (e: IOException){
//                e.printStackTrace()
//            }
//        }).start()


    private fun init() {
        val id = "건대보쌈"
        val waitingnum = "10"

        addTextChangeListener()

        button_store.setOnClickListener{
//            user의 아이디, 비번 입력했던 것은 지워줘야함. (뒤로가기시 복원되지 않도록)
            id_input_editText.text?.clear()
            password_input_editText.text?.clear()
            startActivity<StoreMenu>(
                "storename" to id,
                "waitingnum" to waitingnum
            )
        }


        val customername = "조예린"

        button_customer.setOnClickListener{

            id_input_editText.text?.clear()
            password_input_editText.text?.clear()
            startActivity<CustomerMenu>(
                "customername" to customername
            )
        }

    }

    private fun addTextChangeListener() {
        id_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyId(s.toString())) {
                    id_input_layout.error = "특수문자나 공백은 허용되지 않습니다."
                    button_store.isEnabled = false
                    button_customer.isEnabled = false
                } else {
                    id_input_layout.error = null
                }
                button_store.isEnabled = (id_input_layout.error == null && password_input_layout.error == null)
                button_customer.isEnabled = button_store.isEnabled
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        password_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyPassword(s.toString())) {
                    password_input_layout.error = "영문, 숫자, 특수문자 포함 8자 이상 입력해주세요"
                    button_store.isEnabled = false
                    button_customer.isEnabled = false
                } else {
                    password_input_layout.error = null
                }
//                둘다 알맞게 입력한 경우
                button_store.isEnabled = (id_input_layout.error == null && password_input_layout.error == null)
                button_customer.isEnabled = button_store.isEnabled
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

}