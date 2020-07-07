package com.example.hatewait

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_store_register2.*
//
// 1단계 아이디 + 비번
//
// 2단계 가게이름 + 가게설명 O
//->
// 3단계 가게 도로명주소+  가게전화번호
//->
// 4단계 수용 가능인원 + 가게 설명 + 영업시간(버튼)
//->
//가입완료 환영 메시지 액티비티 or 로그인바로됨
class StoreRegister2 : AppCompatActivity() {

    private val storeNameRegex = Regex("^(?=.*[a-zA-Z가-힣0-9])[a-zA-Z가-힣0-9|\\s|,]{1,}$")
    fun verifyName(storeName : String) : Boolean = storeNameRegex.matches(storeName)
// 가게 간단한 설명은 형식제한 X
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_register2)
        setSupportActionBar(register_toolbar2)
        supportActionBar?.apply {
            //            Set this to true if selecting "home" returns up by a single level in your UI rather than back to the top level or front page.
            setDisplayHomeAsUpEnabled(true)
//            you should also call setHomeActionContentDescription() to provide a correct description of the action for accessibility support.
            setHomeAsUpIndicator(R.drawable.back_icon)
            setHomeActionContentDescription("아이디 & 비밀번호")
            setDisplayShowTitleEnabled(false)
        }
        addTextChangeListener()
        button_continue.setOnClickListener {
            val intent = Intent(this, StoreRegister3::class.java)
            intent.putExtra("STORE_NAME", store_name_input_editText.text.toString())
            intent.putExtra("STORE_DESCRIPTION", store_info_description_editText.text.toString())
            intent.putExtra("STORE_ID", getIntent().getStringExtra("USER_ID"))
            intent.putExtra("STORE_PASSWORD", getIntent().getStringExtra("USER_PASSWORD"))
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
        store_name_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(!verifyName(s.toString())) {
                    store_name_input_layout.error = resources.getString(R.string.store_name_error_message)
                    button_continue.isEnabled = false
                } else {
                    store_name_input_layout.error = null
                    store_name_input_layout.hint = null
                }

                button_continue.isEnabled =
                    (store_name_input_layout.error == null
                            && store_info_description_layout.error == null
                            && !store_info_description_editText.text.isNullOrBlank())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        store_info_description_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrBlank()) {
                    store_info_description_layout.error = resources.getString(R.string.store_description_error_message)
                    button_continue.isEnabled = false
                } else {
                    store_info_description_layout.error = null
                    store_info_description_layout.hint = null
                }
                    button_continue.isEnabled =
                    (store_name_input_layout.error == null
                            && store_info_description_layout.error == null
                            && !store_name_input_editText.text.isNullOrBlank())
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

}