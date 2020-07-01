package com.example.hatewait

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_custom_register2.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class CustomRegister2 : AppCompatActivity() {
    //    한글 2~4자 (공백 허용 X) or 영문 First name 2~10, Last name 2~10
    private val nameRegex = Regex("^[가-힣]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$")
    //    3자리 - 3 or 4자리 - 4자리
    //    첫자리는 반드시 0으로 시작.
    private val phoneRegex = Regex("^[0](\\d{2})(\\d{3,4})(\\d{4})")

    fun verifyName (input_name : String) : Boolean = input_name.matches(nameRegex)
    fun verifyPhoneNumber (input_phone_number : String) : Boolean = input_phone_number.matches(phoneRegex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_register2)

        addTextChangeListener()

        button_finish.setOnClickListener {
            val userId = intent.getStringExtra("user_id")
            val userPassword = intent.getStringExtra("user_password")
            val userName = user_name_input_editText.text.toString()
            val userPhone = user_phone_number_editText.text.toString()
            CustomerRegisterAsyncTask(this@CustomRegister2).execute(userId, userPassword, userName, userPhone)

        }
        setSupportActionBar(register_toolbar)
        supportActionBar?.apply {
            //            Set this to true if selecting "home" returns up by a single level in your UI rather than back to the top level or front page.
            setDisplayHomeAsUpEnabled(true)
//            you should also call setHomeActionContentDescription() to provide a correct description of the action for accessibility support.
            setHomeAsUpIndicator(R.drawable.back_icon)
            setDisplayShowTitleEnabled(false)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    private fun addTextChangeListener() {
        user_name_input_editText.addTextChangedListener(object : TextWatcher {
            //            text에 변화가 있을 때마다
            override fun afterTextChanged(s: Editable?) {
                if(!verifyName(s.toString())) {
                    user_name_input_layout.error = "2~4자 한글 또는 영문이름을 입력해주세요"
                    button_finish.isEnabled = false
                } else {
                    user_name_input_layout.error = null
                    user_name_input_layout.hint = null
                }

                //    둘다 에러가 없을 때 등록 버튼 활성화
                button_finish.isEnabled =
                    (user_name_input_layout.error == null
                            && user_phone_number_layout.error == null
                            && !user_phone_number_editText.text.isNullOrBlank())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        user_phone_number_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyPhoneNumber(s.toString())) {
                    user_phone_number_layout.error = "10~11자리 전화번호를 입력해주세요"
                    button_finish.isEnabled = false
                } else {
                    user_phone_number_layout.error = null
                    user_phone_number_layout.hint = null
                }
                button_finish.isEnabled =
                    (user_name_input_layout.error == null
                            && user_phone_number_layout.error == null
                            && !user_name_input_editText.text.isNullOrBlank())
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun inputLayoutInitialize() {
        user_name_input_editText.text?.clear()
        user_name_input_editText.clearFocus()
        user_name_input_layout.error = null
        user_name_input_layout.hint = "이름을 입력해주세요"
        user_phone_number_editText.text?.clear()
        user_phone_number_editText.clearFocus()
        user_phone_number_layout.error = null
        user_phone_number_layout.hint = "전화번호를 입력해주세요"
    }

    override fun onStop() {
//        inputLayoutInitialize()
        super.onStop()
    }


    class CustomerRegisterAsyncTask(context: CustomRegister2) : AsyncTask<String, Unit, Unit>() {

                private lateinit var clientSocket: Socket
                private lateinit var reader: BufferedReader
                private lateinit var writer: PrintWriter

                private val port = 3000
                private val ip = "192.168.1.166"

                override fun doInBackground(vararg params: String) { // 소켓 연결
                    val userId= params[0]
                    val userPassword = params[1]
                    val userName = params[2]
            val userPhone = params[3]
            try {
                clientSocket = Socket(ip, port)
                writer = PrintWriter(clientSocket!!.getOutputStream(), true)
                reader = BufferedReader(InputStreamReader(clientSocket!!.getInputStream(), "UTF-8"))
                writer!!.println("SIGNUP;MEMBER;$userId;$userName;$userPhone;$userPassword")
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            } finally {
                reader.close()
                writer.close()
                clientSocket.close()
            }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
        }

    }
}