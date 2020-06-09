package com.example.hatewait

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.json.JSONObject


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

        addTextChangeListener()

        button_store.setOnClickListener{
            inputLayoutInitialize()
            startActivity<StoreMenu>()
        }


        button_customer.setOnClickListener{

            inputLayoutInitialize()
            startActivity<CustomerMenu>()
        }

    }



    //    Naver Login Initilization

    // 네아로(네이버 아이디로 로그인) 기능 이용시 전화번호는 따로 입력받아야한다.
    //     전화번호는 Naver 프로필 API에서 제공해주지 않기때문에
    fun naver_login_init() {



        val loginModule = OAuthLogin.getInstance();
        val naverLoginKeyStringArray = resources.getStringArray(R.array.naver_login_api)
//        Client ID, SecretKey, Name
        loginModule.init( this@MainActivity, naverLoginKeyStringArray[0], naverLoginKeyStringArray[1], naverLoginKeyStringArray[2])




        // Offline API 요청은 Network 를 사용하기 때문에 AsyncTask 사용.
        class RequestApiTask : AsyncTask<Void?, Void?, String>() {
            override fun onPreExecute() {
            }
            override fun doInBackground(vararg params: Void?): String? {
//                naver user profile 을 JSON 객체 형태로 얻어옴.
                val url = "https://openapi.naver.com/v1/nid/me"
//                mOAuthLoginHandler로부터 토큰을 따로 받아오지 않으므로
//                별도로 토큰을 얻는 메소드 호출 필요.
                val at: String = loginModule.getAccessToken(this@MainActivity)
//      API 호출   실패시 :  null 반환.
//                성공시: 네이버 유저정보 JSON Format String return
                return loginModule.requestApi(this@MainActivity, at, url)
            }
            override fun onPostExecute(content: String) {
                val resultUserInfoJSON = JSONObject(content).getJSONObject("response")
                val userEmail = resultUserInfoJSON.getString("email")
                val userName = resultUserInfoJSON.getString("name")
                Log.i("userInfo", "이름 : $userName\n이메일: $userEmail")

//                일단은 로그인 계정이 customer 계정이라고 판단할 경우.
                startActivity<CustomerMenu>(
                )
            }

        }


        val loginHandler = object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                if (success) {
                   val accessToken = loginModule.getAccessToken(this@MainActivity)
                    var refreshToken = loginModule.getRefreshToken(this@MainActivity)
                    RequestApiTask().execute()
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