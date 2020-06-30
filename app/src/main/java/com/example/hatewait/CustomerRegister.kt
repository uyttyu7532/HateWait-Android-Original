package com.example.hatewait

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_customer_register.*
import org.jetbrains.anko.startActivity


// App Bar 추가
class CustomerRegister : AppCompatActivity() {
    private val idRegex = Regex("^(?=.*[a-zA-Zㄱ-ㅎ가-힣0-9])[a-zA-Zㄱ-ㅎ가-힣0-9]{1,}$")
    private val passwordRegex =
        Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")
    fun verifyId(input_id: String): Boolean = idRegex.matches(input_id)
    fun verifyPassword(input_password: String): Boolean = passwordRegex.matches(input_password)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_register)
        addTextChangeListener()
        button_continue.setOnClickListener {
            startActivity<CustomRegister2>()
        }
//        Set a Toolbar to act as the ActionBar for this Activity window
        setSupportActionBar(register_toolbar)
// Default Task : App Name + 더보기 // Option Menu has only a'settings'
        supportActionBar?.apply {
//            Set whether home should be displayed as an "up" affordance.
//            Set this to true if selecting "home" returns up by a single level in your UI rather than back to the top level or front page.
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setTitle("로그인 이동")
//            setDisplayShowHomeEnabled(true)
        }
    }

//     Menu 추가 콜백 메소드
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.back_front_button_menu, menu)
        return true
    }

//    메뉴에서 선택된 아이템 클릭 리스너 역할
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.forward_button -> {
                onBackPressed()
            }
        }
        return true
    }

    private fun addTextChangeListener() {
        id_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyId(s.toString())) {
                    id_input_layout.error = "특수문자나 공백은 허용되지 않습니다."
                    button_continue.isEnabled = false
                } else {
                    id_input_layout.error = null
                    id_input_layout.hint = null
                }
                button_continue.isEnabled =
                        (id_input_layout.error == null
                            && password_input_layout.error == null
                            && password_reinput_layout.error == null
                            && !password_input_editText.text.isNullOrBlank()
                            && !password_reinput_editText.text.isNullOrBlank())
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
                    button_continue.isEnabled = false
                } else {
                    password_input_layout.error = null
                    password_input_layout.hint = null
                }
                button_continue.isEnabled =
                        (id_input_layout.error == null
                            && password_input_layout.error == null
                            && password_reinput_layout.error == null
                            && !id_input_editText.text.isNullOrBlank()
                            && !password_reinput_editText.text.isNullOrBlank())
// enabled 상태에 따라 button 색상 ColorPrimary 로 설정할 수 있어야함. (selector 사용 or app Compat Button)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        password_reinput_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(reinputText: Editable?) {
                if (reinputText.toString() != password_input_editText.text.toString()) {
                    password_reinput_layout.error = "비밀번호가 일치하지 않아요"
                    button_continue.isEnabled = false
                } else {
                    password_reinput_layout.error = null
                    password_reinput_layout.hint = null
                }
                button_continue.isEnabled =
                    (id_input_layout.error == null
                            && password_input_layout.error == null
                            && password_reinput_layout.error == null
                            && !id_input_editText.text.isNullOrBlank()
                            && !password_input_editText.text.isNullOrBlank())

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
        id_input_layout.hint = resources.getString(R.string.id_input_hint)
        password_input_editText.text?.clear()
        password_input_layout.clearFocus()
        password_input_layout.error = null
        password_input_layout.hint = resources.getString(R.string.password_input_hint)
        password_reinput_editText.text?.clear()
        password_reinput_layout.error = null
        password_reinput_layout.clearFocus()
        password_reinput_layout.hint = resources.getString(R.string.password_reinput_hint)
    }

    override fun onStop() {
        inputLayoutInitialize()
        super.onStop()
    }

}