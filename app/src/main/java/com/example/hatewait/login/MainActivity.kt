package com.example.hatewait.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R
import com.example.hatewait.customer.CustomerMenu
import com.example.hatewait.signup.CustomerSignUp1
import com.example.hatewait.signup.FindPassWordActivity
import com.example.hatewait.signup.StoreSignUp1
import com.example.hatewait.store.StoreMenu
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var mLastBackPress: Long = 0
    val mBackPressThreshold: Long = 3500

    // 영문,한글,숫자 1자 이상 입력 가능
    private val idRegex = Regex("^(?=.*[a-zA-Zㄱ-ㅎ가-힣0-9])[a-zA-Zㄱ-ㅎ가-힣0-9]{1,}$")

    //    영문, 숫자, 특수문자 포함 8자 이상
    private val passwordRegex =
        Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")

    fun verifyId(input_id: String): Boolean = idRegex.matches(input_id)
    fun verifyPassword(input_password: String): Boolean = passwordRegex.matches(input_password)

    private val storeReference: SharedPreferences by lazy {
//        initalizer Property에 다음 초기화 블록 코드 내용 자체를 저장.
        getSharedPreferences(resources.getString(R.string.store_mode), Context.MODE_PRIVATE)
    }
    private val customerReference: SharedPreferences by lazy {
//        한번도 초기화 되지 않은 val lazy는 getValue() == UNINITIALIZED_VALUE 상태
//        초기화 1번이라도 되면 getValue()호출하여 값을 얻어옴.
        getSharedPreferences(resources.getString(R.string.customer_mode), Context.MODE_PRIVATE)
    }

    private val TAG = "MainActivity"

    @SuppressLint("SourceLockedOrientationActivity")

    private var isCustomerMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        buttonInitialize()
        naverLoginInit()
        addTextChangeListener()
    }

    override fun onStart() {
        autoLoginCheck()
        super.onStart()
    }

    override fun onStop() {
        inputLayoutInitialize()
        super.onStop()
    }


    private fun autoLoginCheck() {
//        둘중 하나라도 자동로그인 설정시 ID/PASSWORD 불러오기 체크박스 체크상태
        auto_login_checkBox.isChecked = (customerReference.getBoolean("AUTO_LOGIN", false)
                or storeReference.getBoolean("AUTO_LOGIN", false))
    }

    private fun buttonInitialize() {
        addTextChangeListener()
        userKindAndAutoLoginCheck()

        user_kind_group.setOnPositionChangedListener {
            userKindAndAutoLoginCheck()
        }
        button_login.setOnClickListener {
            if (isCustomerMode) {
                val editor = customerReference.edit()
                if (auto_login_checkBox.isChecked) {
                    editor.putString("CUSTOMER_ID", id_input_editText.text.toString())
                    editor.putString("CUSTOMER_PASSWORD", password_input_editText.text.toString())
                    editor.putBoolean("AUTO_LOGIN", true)
                    editor.commit()
                } else {
//                    editor.remove("CUSTOMER_ID")
//                    editor.remove("CUSTOMER_PASSWORD")
                    editor.putBoolean("AUTO_LOGIN", false)
                    editor.commit()
                }
                startActivity<CustomerMenu>()

            } else {
                val editor = storeReference.edit()
                if (auto_login_checkBox.isChecked) {
                    editor.putString("STORE_ID", id_input_editText.text.toString())
                    editor.putString("STORE_PASSWORD", password_input_editText.text.toString())
                    editor.putBoolean("AUTO_LOGIN", true)
                    editor.commit()
                } else {
//                    editor.remove("STORE_ID")
//                    editor.remove("STORE_PASSWORD")
                    editor.putBoolean("AUTO_LOGIN", false)
                    editor.commit()
                }
                startActivity<StoreMenu>()
            }
        }
        account_register_textButton.setOnClickListener {
            if (isCustomerMode) {
                startActivity<CustomerSignUp1>()
            } else {
                startActivity<StoreSignUp1>()
            }
        }
        find_password_button.setOnClickListener {
            startActivity<FindPassWordActivity>()
        }
}


    //    Naver Login Initilization

    // 네아로(네이버 아이디로 로그인) 기능 이용시 전화번호는 따로 입력받아야한다.
    //     전화번호는 Naver 프로필 API에서 제공해주지 않기때문에
    private fun naverLoginInit() {


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
        class NaverProfileApiTask : AsyncTask<Void?, Void?, String>() {
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
                    NaverProfileApiTask().execute()
                } else {
                    val errorCode = loginModule.getLastErrorCode(this@MainActivity).code
                    val errorDescription = loginModule.getLastErrorDesc(this@MainActivity)
//                    Toast.makeText(
//                        this@MainActivity,
//                        "errorCode : $errorCode\nerrorMessage : $errorDescription",
//                        Toast.LENGTH_SHORT
//                    ).show()
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
                    id_input_layout.error = resources.getString(R.string.id_input_error)
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
                    password_input_layout.error = resources.getString(R.string.password_input_error)
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

    private fun userKindAndAutoLoginCheck() {
        when (user_kind_group.position) {
            0 -> {
                if (storeReference.getBoolean("AUTO_LOGIN", false)) {
                    id_input_editText.setText(storeReference.getString("STORE_ID", null))
                    password_input_editText.setText(
                        storeReference.getString(
                            "STORE_PASSWORD",
                            null
                        )
                    )
                }
                isCustomerMode = false
            }
            1 -> {
                if (customerReference.getBoolean("AUTO_LOGIN", false)) {
                    id_input_editText.setText(customerReference.getString("CUSTOMER_ID", null))
                    password_input_editText.setText(
                        customerReference.getString(
                            "CUSTOMER_PASSWORD",
                            null
                        )
                    )
                }
                isCustomerMode = true
            }
        }
    }

    private fun inputLayoutInitialize() {
        id_input_editText.text?.clear()
        id_input_layout.error = null
        id_input_layout.clearFocus()
        id_input_layout.hint = "이메일 아이디를 입력해주세요"
        password_input_editText.text?.clear()
        password_input_layout.clearFocus()
        password_input_layout.error = null
        password_input_layout.hint = "비밀번호를 입력해주세요"
    }

    override fun onBackPressed() {
        val pressBackToast = Toasty.normal(this, "한번 더 뒤로가기 키를 누르면 종료됩니다.", Toasty.LENGTH_SHORT)

        val currentTime = System.currentTimeMillis()
        if (Math.abs(currentTime - mLastBackPress) > mBackPressThreshold) {
            mLastBackPress = currentTime
            pressBackToast.show()
        } else {
            pressBackToast.cancel()
            super.onBackPressed()
        }
    }
}