package com.example.hatewait

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessaging
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private val idRegex = Regex("^(?=.*[a-zA-Zㄱ-ㅎ가-힣0-9])[a-zA-Zㄱ-ㅎ가-힣0-9]{1,}$")
    private val passwordRegex =
        Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")

    fun verifyId(input_id: String): Boolean = idRegex.matches(input_id)
    fun verifyPassword(input_password: String): Boolean = passwordRegex.matches(input_password)

    private val TAG = "MainActivity"

    @SuppressLint("SourceLockedOrientationActivity")

    private var isCustomerMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        init()
        naver_login_init()
        addTextChangeListener()
    }

    override fun onStop() {
        inputLayoutInitialize()
        super.onStop()
    }


    private fun init() {

        addTextChangeListener()

        user_kind_group.setOnPositionChangedListener {
            when (user_kind_group.position) {
                0 -> isCustomerMode = false
                1 -> isCustomerMode = true
                else -> true
            }
        }
//        Logic 추가. Customer? Store?
        button_login.setOnClickListener {

            if (isCustomerMode) {
                startActivity<CustomerMenu>()
            } else {
                startActivity<StoreMenu>()
            }
        }
        account_register_textButton.setOnClickListener {
            if (isCustomerMode) {
                startActivity<CustomerRegister>()
            } else {
                startActivity<StoreRegister>()
            }
        }
    }


    //    Naver Login Initilization

    // 네아로(네이버 아이디로 로그인) 기능 이용시 전화번호는 따로 입력받아야한다.
    //     전화번호는 Naver 프로필 API에서 제공해주지 않기때문에
    private fun naver_login_init() {


        val loginModule = OAuthLogin.getInstance();
        val naverLoginKeyStringArray = resources.getStringArray(R.array.naver_login_api)
//        Client ID, SecretKey, Name
        loginModule.init(
            this@MainActivity,
            naverLoginKeyStringArray[0],
            naverLoginKeyStringArray[1],
            naverLoginKeyStringArray[2]
        )




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
                startActivity<CustomerMenu>()
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
                    Toast.makeText(
                        this@MainActivity,
                        "errorCode : $errorCode\nerrorMessage : $errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
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
                    button_login.isEnabled = false
                } else {
                    id_input_layout.error = null
                    id_input_layout.hint = null
                }
                button_login.isEnabled =
                    (id_input_layout.error == null && password_input_layout.error == null && !password_input_editText.text.isNullOrBlank())
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
                    button_login.isEnabled = false
                } else {
                    password_input_layout.error = null
                    password_input_layout.hint = null
                }
//                둘다 알맞게 입력한 경우
                button_login.isEnabled =
                    (id_input_layout.error == null && password_input_layout.error == null && !id_input_editText.text.isNullOrBlank())
// enabled 상태에 따라 button 색상 ColorPrimary 로 설정할 수 있어야함. (selector 사용 or app Compat Button)
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
        id_input_layout.hint = "아이디를 입력해주세요"
        password_input_editText.text?.clear()
        password_input_layout.clearFocus()
        password_input_layout.error = null
        password_input_layout.hint = "비밀번호를 입력해주세요"
    }
}