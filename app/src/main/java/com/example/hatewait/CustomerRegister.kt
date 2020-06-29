package com.example.hatewait

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_customer_register.*
import org.jetbrains.anko.startActivity

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
                    password_reinput_layout.error = "패스워드가 일치하지 않아요"
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