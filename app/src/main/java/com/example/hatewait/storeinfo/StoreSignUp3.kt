package com.example.hatewait.storeinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_store_register3.*

// 1단계 이메일 , 인증번호 (네아로면 생략)
// 2단계 비번, 비번확인
// 3단계 가게이름, 전화번호, 도로명주소
// 4단계 가게 영업시간, 인원 수, 문구
// 가입완료 환영 메시지 액티비티 or 로그인바로됨

class StoreSignUp3 : AppCompatActivity() {

    private val storeNameRegex = Regex("^(?=.*[a-zA-Z가-힣0-9])[a-zA-Z가-힣0-9|\\s|,]{1,}$")
    private val storePhoneRegex = Regex("^[0](\\d{2})(\\d{3,4})(\\d{3,4})")
    private val storeAddressRegex = Regex("^[가-힣]+[가-힣a-zA-Z0-9|\\-|,|\\s]{1,50}$")
    fun verifyName(storeName: String): Boolean = storeNameRegex.matches(storeName)
    fun verifyPhone(storePhone: String): Boolean = storePhoneRegex.matches(storePhone)
    fun verifyAddress(storeAddress: String): Boolean = storeAddressRegex.matches(storeAddress)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_register3)
        setSupportActionBar(register_toolbar2)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setHomeActionContentDescription("아이디 & 비밀번호")
            setDisplayShowTitleEnabled(false)
        }
        addTextChangeListener()
        button_continue.setOnClickListener {
            val intent = Intent(this, StoreSignUp4::class.java)
            intent.putExtra("STORE_ID", getIntent().getStringExtra("USER_ID"))
            intent.putExtra("STORE_PASSWORD", getIntent().getStringExtra("USER_PASSWORD"))
            intent.putExtra("STORE_NAME", store_name_input_editText.text.toString())
            intent.putExtra("STORE_PHONE", store_phone_editText.text.toString())
            intent.putExtra("STORE_ADDRESS", store_address_input_editText.text.toString())
            startActivity(intent)
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
        // 가게명
        store_name_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyName(s.toString())) {
                    store_name_input_layout.error =
                        resources.getString(R.string.store_name_error_message)
                    button_continue.isEnabled = false
                } else {
                    store_name_input_layout.error = null
                    store_name_input_layout.hint = null
                }

                button_continue.isEnabled =
                    (store_name_input_layout.error == null
                            && store_phone_layout.error == null
                            && !store_phone_editText.text.isNullOrBlank()
                            && store_address_input_editText.error == null
                            && !store_address_input_editText.text.isNullOrBlank())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        // 전화번호
        store_phone_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(storePhone: Editable?) {
                if (!verifyPhone(storePhone.toString())) {
                    store_phone_layout.error =
                        resources.getString(R.string.store_phone_error_message)
                    button_continue.isEnabled = false
                } else {
                    store_phone_layout.error = null
                    store_phone_layout.hint = null
                }
                button_continue.isEnabled =
                    (store_name_input_layout.error == null
                            && store_phone_layout.error == null
                            && !store_phone_editText.text.isNullOrBlank()
                            && store_address_input_editText.error == null
                            && !store_address_input_editText.text.isNullOrBlank())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // 도로명주소
        store_address_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(storeAddress: Editable?) {
                if (!verifyAddress(storeAddress.toString())) {
                    store_address_input_layout.error = "하이픈(-)과 콤마(,) 제외한 특수문자는 허용되지않습니다."
                    button_continue.isEnabled = false
                } else {
                    store_address_input_layout.error = null
                    store_address_input_layout.hint = null
                }
                button_continue.isEnabled =
                    (store_name_input_layout.error == null
                            && store_phone_layout.error == null
                            && !store_phone_editText.text.isNullOrBlank()
                            && store_address_input_editText.error == null
                            && !store_address_input_editText.text.isNullOrBlank())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }

}