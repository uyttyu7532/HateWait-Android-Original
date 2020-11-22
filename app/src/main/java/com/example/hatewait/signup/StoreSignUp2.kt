package com.example.hatewait.signup

import LottieDialogFragment.Companion.newInstance
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R
import com.example.hatewait.model.StoreSignUpResponseData
import com.example.hatewait.retrofit2.MyApi
import kotlinx.android.synthetic.main.activity_signup1.*
import kotlinx.android.synthetic.main.activity_signup1.register_toolbar
import kotlinx.android.synthetic.main.activity_signup2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// 1단계 이메일 , 인증번호 (네아로면 생략)
// 2단계 아아디, 비번, 비번확인
// 3단계 가게이름, 전화번호, 도로명주소
// 4단계 가게 영업시간, 인원 수, 문구
// 가입완료 환영 메시지 액티비티 or 로그인바로됨

lateinit var _storeSignUp2: Activity
private lateinit var mcontext: Context

class StoreSignUp2 : AppCompatActivity() {

    private var checkMailCode = false;

    private val idRegex = Regex("^(?=.*[a-zA-Zㄱ-ㅎ가-힣0-9])[a-zA-Zㄱ-ㅎ가-힣0-9]{1,}$")
    private val passwordRegex =
        Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")

    fun verifyId(input_id: String): Boolean = idRegex.matches(input_id)
    fun verifyPassword(input_password: String): Boolean = passwordRegex.matches(input_password)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup2)

        _storeSignUp2 = this
        mcontext = this
        addTextChangeListener()


        button_continue.setOnClickListener {

            MyApi.SignUpService.requestCheckStoreId(id_input_edit_text.text.toString())
                .enqueue(object : Callback<MyApi.onlyMessageResponseData> {
                    override fun onFailure(
                        call: Call<MyApi.onlyMessageResponseData>,
                        t: Throwable
                    ) {

                        Log.d("retrofit2 가게 아이디확인 :: ", "연결실패 $t")
                    }

                    override fun onResponse(
                        call: Call<MyApi.onlyMessageResponseData>,
                        response: Response<MyApi.onlyMessageResponseData>
                    ) {
                        newInstance().dismiss()
                        Log.d(
                            "retrofit2 가게 아이디확인 ::",
                            response.code().toString() + response.body().toString()
                        )

                        when (response.code()) {
                            200 -> {
                                checkMailCode = false
                                val intent = Intent(mcontext, StoreSignUp3::class.java)
                                intent.putExtra(
                                    "STORE_EMAIL",
                                    getIntent().getStringExtra("STORE_EMAIL")
                                )
                                intent.putExtra("STORE_ID", id_input_edit_text.text.toString())
                                intent.putExtra(
                                    "STORE_PASSWORD",
                                    password_input_editText.text.toString()
                                )
                                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                startActivity(intent)
                            }
                            409 -> {
                                Toast.makeText(mcontext, "이미 가입된 아이디입니다.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                    }
                }
                )


        }

        setSupportActionBar(register_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setHomeActionContentDescription("로그인 화면 이동")
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.back_front_button_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.forward_button -> {
                if (!button_continue.isEnabled) {
                    return false
                } else {
                    button_continue.performClick()
                }
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }


    private fun addTextChangeListener() {

        // 아이디
        id_input_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyId(s.toString())) {
                    id_input_layout.error = "아이디를 입력해주세요"
                    button_continue.isEnabled = false
                } else {
                    id_input_layout.error = null
                    id_input_layout.hint = null
                }

                button_continue.isEnabled =
                    (id_input_layout.error == null
                            && password_input_layout.error == null
                            && password_reinput_layout.error == null
                            && !password_reinput_editText.text.isNullOrBlank())

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        // 비밀번호
        password_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyPassword(s.toString())) {
                    password_input_layout.error = resources.getString(R.string.password_input_error)
                    button_continue.isEnabled = false
                } else {
                    password_input_layout.error = null
                    password_input_layout.hint = null
                }
                if (password_input_editText.text.toString() != password_reinput_editText.text.toString()) {
                    password_reinput_layout.error =
                        resources.getString(R.string.password_reinput_error)
                    button_continue.isEnabled = false
                } else {
                    password_reinput_layout.error = null
                    password_reinput_layout.hint = null
                }
                button_continue.isEnabled =
                    (id_input_layout.error == null
                            && password_input_layout.error == null
                            && password_reinput_layout.error == null
                            && !password_reinput_editText.text.isNullOrBlank())
                // enabled 상태에 따라 button 색상 ColorPrimary 로 설정할 수 있어야함. (selector 사용 or app Compat Button)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // 비밀번호 재입력
        password_reinput_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(reinputText: Editable?) {
                if (reinputText.toString() != password_input_editText.text.toString()) {
                    password_reinput_layout.error =
                        resources.getString(R.string.password_reinput_error)
                    button_continue.isEnabled = false
                } else {
                    password_reinput_layout.error = null
                    password_reinput_layout.hint = null
                }
                button_continue.isEnabled =
                    (id_input_layout.error == null
                            && password_input_layout.error == null
                            && password_reinput_layout.error == null
                            && !password_reinput_editText.text.isNullOrBlank())

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}