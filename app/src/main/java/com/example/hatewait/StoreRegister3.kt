package com.example.hatewait

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_store_register3.*

class StoreRegister3 : AppCompatActivity() {
//    도로명주소 영문, 숫자, 하이픈 가능 (부산에 'APEC로' 존재)
//    한글로 시작하고 한글,영문,숫자,공백(띄어쓰기) 허용, 특수문자 X
    private val storeAddressRegex = Regex("^[가-힣]+[가-힣a-zA-Z0-9|\\-|,|\\s]{1,50}$")
    private val storePhoneRegex = Regex("^[0](\\d{2})(\\d{3,4})(\\d{3,4})")
    fun verifyPhone(storePhone : String) : Boolean = storePhoneRegex.matches(storePhone)
    fun verifyAddres(storeAddress : String) : Boolean = storeAddressRegex.matches(storeAddress)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_register3)
        setSupportActionBar(register_toolbar3)
        supportActionBar?.apply {
//            Set this to true if selecting "home" returns up by a single level in your UI
//            rather than back to the top level or front page.
            setDisplayHomeAsUpEnabled(true)
//            you should also call setHomeActionContentDescription() to provide a correct description of the action for accessibility support.
            setHomeAsUpIndicator(R.drawable.back_icon)
            setHomeActionContentDescription("가게명 & 가게설명")
            setDisplayShowTitleEnabled(false)
        }
        addTextChangeListener()
        button_continue.setOnClickListener {

            var result = ""
            result += intent.getStringExtra("STORE_NAME")
            result += intent.getStringExtra("STORE_DESCRIPTION")
            result += intent.getStringExtra("STORE_ID")
            result += intent.getStringExtra("STORE_PASSWORD")
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
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
        store_address_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(storeAddress: Editable?) {
                if (!verifyAddres(storeAddress.toString())) {
                    store_address_input_layout.error = "하이픈(-)과 콤마(,) 제외한 특수문자는 허용되지않습니다."
                    button_continue.isEnabled = false
                } else {
                    store_address_input_layout.error = null
                    store_address_input_layout.hint = null
                }
                button_continue.isEnabled =
                        store_address_input_layout.error == null
                        && store_phone_layout.error == null
                        && !store_phone_editText.text.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        store_phone_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(storePhone: Editable?) {
                if (!verifyPhone(storePhone.toString())) {
                    store_phone_layout.error = resources.getString(R.string.store_phone_error_message)
                    button_continue.isEnabled = false
                } else {
                    store_phone_layout.error = null
                    store_phone_layout.hint = null
                }
                button_continue.isEnabled =
                            store_phone_layout.error == null
                                    && store_address_input_layout.error == null
                                    && !store_address_input_editText.text.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

}