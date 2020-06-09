package com.example.hatewait

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity


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
        naver_login_init()
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
            inputLayoutInitialize()
            startActivity<StoreMenu>(
                "storename" to id,
                "waitingnum" to waitingnum
            )
        }


        val customername = "조예린"

        button_customer.setOnClickListener{

            inputLayoutInitialize()
            startActivity<CustomerMenu>(
                "customername" to customername
            )
        }

    }

    fun naver_login_init() {
        val loginModule = OAuthLogin.getInstance();
        val naverLoginKeyStringArray = resources.getStringArray(R.array.naver_login_api)
//        Client ID, SecretKey, Name
        loginModule.init( this@MainActivity, naverLoginKeyStringArray[0], naverLoginKeyStringArray[1], naverLoginKeyStringArray[2])

        val loginHandler = object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                if (success) {
                   val accessToken = loginModule.getAccessToken(this@MainActivity)
                    var refreshToken = loginModule.getRefreshToken(this@MainActivity)
                } else {
                    val errorCode = loginModule.getLastErrorCode(this@MainActivity).code
                    val errorDescription = loginModule.getLastErrorDesc(this@MainActivity)
                    Toast.makeText(this@MainActivity, "errorCode : $errorCode\nerrorMessage : $errorDescription", Toast.LENGTH_SHORT).show()
                }
            }
        }

        naver_login_button.setOnClickListener {
            loginModule.startOauthLoginActivity(this@MainActivity, loginHandler)
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
//                button_store.isEnabled = (id_input_layout.error == null && password_input_layout.error == null && !password_input_editText.text.isNullOrBlank())
//                button_customer.isEnabled = button_store.isEnabled
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
//                button_store.isEnabled = (id_input_layout.error == null && password_input_layout.error == null && !id_input_editText.text.isNullOrBlank())
//                button_customer.isEnabled = button_store.isEnabled
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
    private fun inputLayoutInitialize() {
        id_input_editText.text?.clear()
        id_input_layout.error = null
        id_input_layout.clearFocus()
        password_input_editText.text?.clear()
        password_input_layout.clearFocus()
        password_input_layout.error = null
    }
}