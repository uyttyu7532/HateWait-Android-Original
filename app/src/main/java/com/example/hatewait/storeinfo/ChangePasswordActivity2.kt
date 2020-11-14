package com.example.hatewait.storeinfo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.hatewait.R
import com.example.hatewait.retrofit2.MyApi
import kotlinx.android.synthetic.main.activity_change_password2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var mcontext: Context

class ChangePasswordActivity2 : AppCompatActivity() {


    private val passwordRegex =
        Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")

    fun verifyPassword(input_password: String): Boolean = passwordRegex.matches(input_password)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password2)

        setSupportActionBar(register_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setDisplayShowTitleEnabled(false)
        }


        mcontext = this.applicationContext
        addTextChangeListener()

        val userId = intent.getStringExtra("USER_ID")
        val isStore = intent.getBooleanExtra("IS_STORE", true)

        button_continue.setOnClickListener {

            if (isStore) { // 가게
                MyApi.UpdateService.requestStorePassWordUpdate(
                    userId,
                    password_input_edit_text.text.toString()
                )
                    .enqueue(object : Callback<MyApi.onlyMessageResponseData> {
                        override fun onFailure(
                            call: Call<MyApi.onlyMessageResponseData>,
                            t: Throwable
                        ) {
                            Log.d("retrofit2 가게비밀번호변경 :: ", "연결실패 $t")
                        }

                        override fun onResponse(
                            call: Call<MyApi.onlyMessageResponseData>,
                            response: Response<MyApi.onlyMessageResponseData>
                        ) {
                            var data: MyApi.onlyMessageResponseData? = response?.body()
                            Log.d(
                                "retrofit2 가게비밀번호 변경 ::",
                                response.code().toString() + response.body().toString()
                            )
                            when (response.code()) {
                                200 -> {
                                    Toast.makeText(
                                        mcontext,
                                        "비밀번호가 성공적으로 변경되었습니다.",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    finish()
                                    _ChangePassword1.finish()
                                }
                            }
                        }
                    }
                    )
            } else { // 손님
                MyApi.UpdateService.requestMemberPassWordUpdate(
                    userId,
                    password_input_edit_text.text.toString()
                )
                    .enqueue(object : Callback<MyApi.onlyMessageResponseData> {
                        override fun onFailure(
                            call: Call<MyApi.onlyMessageResponseData>,
                            t: Throwable
                        ) {
                            Log.d("retrofit2 손님비밀번호변경 :: ", "연결실패 $t")
                        }

                        override fun onResponse(
                            call: Call<MyApi.onlyMessageResponseData>,
                            response: Response<MyApi.onlyMessageResponseData>
                        ) {
                            var data: MyApi.onlyMessageResponseData? = response?.body()
                            Log.d(
                                "retrofit2 손님비밀번호변경 ::",
                                response.code().toString() + response.body().toString()
                            )
                            when (response.code()) {
                                20 -> {
                                    Toast.makeText(
                                        mcontext,
                                        "비밀번호가 성공적으로 변경되었습니다.",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    finish()
                                    _ChangePassword1.finish()
                                }
                            }
                        }
                    }
                    )
            }
        }

        setSupportActionBar(register_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setHomeActionContentDescription("로그인 화면 이동")
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    private fun addTextChangeListener() {

        // 비밀번호
        password_input_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyPassword(s.toString())) {
                    password_input_layout.error = resources.getString(R.string.password_input_error)
                    button_continue.isEnabled = false
                } else {
                    password_input_layout.error = null
                    password_input_layout.hint = null
                }
                if (password_input_edit_text.text.toString() != password_reinput_edit_text.text.toString()) {
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
                            && !password_reinput_edit_text.text.isNullOrBlank())
                // enabled 상태에 따라 button 색상 ColorPrimary 로 설정할 수 있어야함. (selector 사용 or app Compat Button)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // 비밀번호 재입력
        password_reinput_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(reinputText: Editable?) {
                if (reinputText.toString() != password_input_edit_text.text.toString()) {
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
                            && !password_reinput_edit_text.text.isNullOrBlank())

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}