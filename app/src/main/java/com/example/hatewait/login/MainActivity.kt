package com.example.hatewait.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R
import com.example.hatewait.member.MemberMenu
import com.example.hatewait.model.MemberLoginRequestData
import com.example.hatewait.model.MemberLoginResponseData
import com.example.hatewait.model.StoreLoginRequestData
import com.example.hatewait.model.StoreLoginResponseData
import com.example.hatewait.retrofit2.MyApi
import com.example.hatewait.signup.SelectSignUp
import com.example.hatewait.store.StoreMenu
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.data.OAuthLoginState
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var memberInfo: MemberLoginResponseData? = null
var storeInfo: StoreLoginResponseData? = null
lateinit var mContext: Context

class MainActivity : AppCompatActivity() {
    var mLastBackPress: Long = 0
    val mBackPressThreshold: Long = 3500

    // 영문,한글,숫자 1자 이상 입력 가능
    private val idRegex = Regex("^(?=.*[a-zA-Zㄱ-ㅎ가-힣0-9])[a-zA-Zㄱ-ㅎ가-힣0-9]{1,}$")
//    private val idRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
//    private val idRegex = Regex(".*")

    //    영문, 숫자, 특수문자 포함 8자 이상
    private val passwordRegex =
        Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")
//    private val passwordRegex = Regex(".*")

    fun verifyId(input_id: String): Boolean = idRegex.matches(input_id)
    fun verifyPassword(input_password: String): Boolean = passwordRegex.matches(input_password)

    private val storeReference: SharedPreferences by lazy {
//        initalizer Property에 다음 초기
//        화 블록 코드 내용 자체를 저장.
        getSharedPreferences(resources.getString(R.string.store_mode), Context.MODE_PRIVATE)
    }
    private val memberReference: SharedPreferences by lazy {
//        한번도 초기화 되지 않은 val lazy는 getValue() == UNINITIALIZED_VALUE 상태
//        초기화 1번이라도 되면 getValue()호출하여 값을 얻어옴.
        getSharedPreferences(resources.getString(R.string.customer_mode), Context.MODE_PRIVATE)
    }


    @SuppressLint("SourceLockedOrientationActivity")

    private var isCustomerMode = true

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this
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
        auto_login_checkBox.isChecked = (memberReference.getBoolean("AUTO_LOGIN", false)
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
                val editor = memberReference.edit()
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


                var customerLoginData = MemberLoginRequestData(
                    id_input_editText.text.toString(),
                    password_input_editText.text.toString()
                )

                MyApi.LoginService.requestMemberLogin(customerLoginData)
                    .enqueue(object : Callback<MemberLoginResponseData> {
                        override fun onFailure(call: Call<MemberLoginResponseData>, t: Throwable) {
                            Log.d("retrofit2 손님로그인 :: ", "연결실패 $t")
                            Toast.makeText(mContext, "서버 연결실패", Toast.LENGTH_SHORT).show()
                            // TODO 주석처리해야함
                            startActivity<MemberMenu>()
                        }

                        override fun onResponse(
                            call: Call<MemberLoginResponseData>,
                            response: Response<MemberLoginResponseData>
                        ) {
                            Log.d(
                                "retrofit2 손님로그인 ::",
                                response.code().toString() + response.body().toString()
                            )
                            when (response.code()) {
                                200 -> {
                                    var data: MemberLoginResponseData? = response?.body()
                                    memberInfo = data

                                    startActivity<MemberMenu>()
                                }
                                409 -> {
                                    Toast.makeText(
                                        mContext,
                                        "아이디나 비밀번호를 확인해주세요.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                500 -> {
                                    Toast.makeText(mContext, "서버 오류", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                    }
                    )


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

                var storeLoginData = StoreLoginRequestData(
                    id_input_editText.text.toString(),
                    password_input_editText.text.toString()
                )

                MyApi.LoginService.requestStoreLogin(storeLoginData)
                    .enqueue(object : Callback<StoreLoginResponseData> {
                        override fun onFailure(call: Call<StoreLoginResponseData>, t: Throwable) {
                            Log.d("retrofit2 가게로그인 :: ", "연결실패 $t")

                            // TODO 주석처리해야함
                            startActivity<StoreMenu>()
                        }

                        override fun onResponse(
                            call: Call<StoreLoginResponseData>,
                            response: Response<StoreLoginResponseData>
                        ) {
                            Log.d(
                                "retrofit2 가게로그인 ::",
                                response.code().toString() + response.body().toString()
                            )
                            when (response.code()) {
                                200 -> {
                                    var data: StoreLoginResponseData? = response?.body()
                                    storeInfo = data

                                    startActivity<StoreMenu>()
                                }
                                409 -> {
                                    Toast.makeText(
                                        mContext,
                                        "아이디나 비밀번호를 확인해주세요.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                500 -> {
                                    Toast.makeText(mContext, "서버 오류", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                    }
                    )
            }
        }

        account_register_textButton.setOnClickListener {
            val intent = Intent(this, SelectSignUp::class.java)
            intent.putExtra("isFromNaver", false)
            intent.putExtra("isSignUp", true)
            this.startActivity(intent)
        }

        find_password_button.setOnClickListener {
            val intent = Intent(this, SelectSignUp::class.java)
            intent.putExtra("isSignUp", false)
            this.startActivity(intent)
        }
    }


    private fun addTextChangeListener() {
        id_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyId(s.toString())) {
//                    id_input_layout.error = "아이디를 입력해주세요"
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
                if (memberReference.getBoolean("AUTO_LOGIN", false)) {
                    id_input_editText.setText(memberReference.getString("CUSTOMER_ID", null))
                    password_input_editText.setText(
                        memberReference.getString(
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
        id_input_layout.hint = "아이디를 입력해주세요"
        password_input_editText.text?.clear()
        password_input_layout.clearFocus()
        password_input_layout.error = null
        password_input_layout.hint = "비밀번호를 입력해주세요"
    }

    override fun onBackPressed() {
        val pressBackToast = Toast.makeText(this, "한번 더 뒤로가기 키를 누르면 종료됩니다.", Toast.LENGTH_SHORT)

        val currentTime = System.currentTimeMillis()
        if (Math.abs(currentTime - mLastBackPress) > mBackPressThreshold) {
            mLastBackPress = currentTime
            pressBackToast.show()
        } else {
            pressBackToast.cancel()
            super.onBackPressed()
        }
    }


    private fun naverLoginInit() {

        val loginModule = OAuthLogin.getInstance()
        val naverLoginKeyStringArray = resources.getStringArray(R.array.naver_login_api)
        loginModule.init(
            this@MainActivity,
            naverLoginKeyStringArray[0],
            naverLoginKeyStringArray[1],
            naverLoginKeyStringArray[2]
        )
        Log.d("네아로1", "${loginModule.getState(this@MainActivity)}")



        // Offline API 요청은 Network 를 사용하기 때문에 AsyncTask 사용.
        class NaverProfileApiTask : AsyncTask<Void?, Void?, String>() {
            override fun onPreExecute() {
            }

            override fun doInBackground(vararg params: Void?): String? {
                if (OAuthLoginState.NEED_REFRESH_TOKEN == loginModule.getState(mContext)) {  // 네이버
                    Log.d("네아로", loginModule.getState(mContext).toString())
                    loginModule.refreshAccessToken(mContext);
                }
//                naver user profile 을 JSON 객체 형태로 얻어옴.
                //                mOAuthLoginHandler로부터 토큰을 따로 받아오지 않으므로
//                별도로 토큰을 얻는 메소드 호출 필요.
                val url = "https://openapi.naver.com/v1/nid/me"
                val at: String = loginModule.getAccessToken(this@MainActivity)
//      API 호출   실패시 :  null 반환.
//                성공시: 네이버 유저정보 JSON Format String return
                Log.d("네아로", "$at ${loginModule.getExpiresAt(this@MainActivity)}")

                return loginModule.requestApi(this@MainActivity, at, url)
            }

            override fun onPostExecute(content: String) {
                val resultUserInfoJSON = JSONObject(content).getJSONObject("response")
                val naverUserId = resultUserInfoJSON.getString("id")
                val naverUserEmail = resultUserInfoJSON.getString("email")
                val naverUserName = resultUserInfoJSON.getString("name")
                Log.i("네아로", resultUserInfoJSON.toString())


//                if (DB에 회원이 있다면) {
//                    if (이메일로 가입된 회원입니다.)
//                        if (가게회원) {
//                            startActivity<StoreMenu>()
//                        }
//                    if (손님회원) {
//                        startActivity<MemberMenu>()
//                    }
//                    DB에 at 을 업데이트하고 로그인
//                } else {
                val intent = Intent(mContext, SelectSignUp::class.java)
                intent.putExtra("isFromNaver", true)
                intent.putExtra("isSignUp", true)
                intent.putExtra("naverUserId", naverUserId)
                intent.putExtra("naverUserEmail", naverUserEmail)
                intent.putExtra("naverUserName", naverUserName)
                mContext.startActivity(intent)
//                }


            }

        }


        val loginHandler = @SuppressLint("HandlerLeak")
        object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                if (success) {
                    val accessToken = loginModule.getAccessToken(this@MainActivity)
                    var refreshToken = loginModule.getRefreshToken(this@MainActivity)
                    Log.d("네아로2", "${loginModule.getState(this@MainActivity)}")
                    NaverProfileApiTask().execute()
                } else {
                    val errorCode = loginModule.getLastErrorCode(this@MainActivity).code
                    val errorDescription = loginModule.getLastErrorDesc(this@MainActivity)
                    Log.d("네아로", "errorCode : $errorCode\nerrorMessage : $errorDescription")
                }
            }
        }


        naver_login_button.setOnClickListener {
            // 갱신 토큰이 잇는 지 확인
            // 성공 -> OAuthLoginHandler 객체 호출
            // 실패 -> 로그인 창
            loginModule.startOauthLoginActivity(this@MainActivity, loginHandler)
        }

    }

}

