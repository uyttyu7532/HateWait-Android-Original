package com.example.hatewait.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_signup1.*
import kotlinx.android.synthetic.main.activity_signup2.*
import kotlinx.android.synthetic.main.activity_signup2.register_toolbar


// 1단계 이메일 , 인증번호 (네아로면 생략)
// 2단계 아이디, 비번, 비번확인
// 3단계 이름(네아로 생략), 전화번호
// 가입완료 환영 메시지 액티비티 or 로그인바로됨

lateinit var _customerSignUp2: Activity
private lateinit var mcontext: Context

class CustomerSignUp2 : AppCompatActivity() {

    private var checkMailCode = false

    private val idRegex = Regex("^(?=.*[a-zA-Zㄱ-ㅎ가-힣0-9])[a-zA-Zㄱ-ㅎ가-힣0-9]{1,}$")
    private val passwordRegex =
        Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")

    fun verifyId(input_id: String): Boolean = idRegex.matches(input_id)
    fun verifyPassword(input_password: String): Boolean = passwordRegex.matches(input_password)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup2)
        _customerSignUp2 = this
        mcontext = this
        addTextChangeListener()


        button_continue.setOnClickListener {
            checkMailCode = false
            val intent = Intent(this, CustomerSignUp3::class.java)
            intent.putExtra("USER_EMAIL", getIntent().getStringExtra("USER_EMAIL"))
            intent.putExtra("USER_ID", id_input_edit_text.text.toString())
            intent.putExtra("USER_PASSWORD", password_input_editText.text.toString())
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
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
                    id_input_layout.error == null

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
                    (password_input_layout.error == null
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
                    (password_input_layout.error == null
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