package com.example.hatewait

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_store_register2.*

class StoreRegister2 : AppCompatActivity() {
    private val storePhoneRegex = Regex("^[0](\\d{2})(\\d{3,4})(\\d{3,4})")
    private val storeNameRegex = Regex("^(?=.*[a-zA-Z가-힣0-9])[a-zA-Z가-힣0-9|\\s|,]{1,}$")
    fun verifyName(storeName : String) : Boolean = storeNameRegex.matches(storeName)
    fun verifyPhone(storePhone : String) : Boolean = storePhoneRegex.matches(storePhone)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_register2)
        setSupportActionBar(register_toolbar2)
        supportActionBar?.apply {
            //            Set this to true if selecting "home" returns up by a single level in your UI rather than back to the top level or front page.
            setDisplayHomeAsUpEnabled(true)
//            you should also call setHomeActionContentDescription() to provide a correct description of the action for accessibility support.
            setHomeAsUpIndicator(R.drawable.back_icon)
            setHomeActionContentDescription("아이디 패스워드 설정")
            setDisplayShowTitleEnabled(false)
        }
        addTextChangeListener()
        button_continue.setOnClickListener {

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
                            && store_phone_number_layout.error == null
                            && !store_phone_number_editText.text.isNullOrBlank())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        store_phone_number_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyPhone(s.toString())) {
                    store_phone_number_layout.error = resources.getString(R.string.store_phone_error_message)
                    button_continue.isEnabled = false
                } else {
                    store_phone_number_layout.error = null
                    store_phone_number_layout.hint = null
                }
                    button_continue.isEnabled =
                    (store_name_input_layout.error == null
                            && store_phone_number_layout.error == null
                            && !store_name_input_editText.text.isNullOrBlank())
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

}