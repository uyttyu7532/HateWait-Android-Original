package com.example.hatewait.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R
import com.example.hatewait.mail.SendMail
import com.example.hatewait.mail.countDown
import com.example.hatewait.mail.emailCode
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_signup1.*


// 1단계 이메일 , 인증번호 (네아로면 생략)
// 2단계 비번, 비번확인
// 3단계 이름(네아로 생략), 전화번호
// 가입완료 환영 메시지 액티비티 or 로그인바로됨

lateinit var _customerSignUp1:Activity
private lateinit var mcontext: Context
private lateinit var senderTo: String

class CustomerSignUp1 : AppCompatActivity() {

    var customView: View? = null
    private val emailRegex = Regex(
        ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    )

    fun verifyEmail(input_email: String): Boolean = emailRegex.matches(input_email)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup1)
        _customerSignUp1 = this
        mcontext = this
        addTextChangeListener()


        check_email_button.setOnClickListener {
            // TODO 디비에서 존재하는 아이디인지 확인 후

            //인터넷 사용권한 허가
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .permitDiskReads()
                    .permitDiskWrites()
                    .permitNetwork().build()
            )

            senderTo = email_input_edit_text.text.toString()
            SendMail().sendSecurityCode(mcontext, senderTo)

            showSettingPopup()
        }

        setSupportActionBar(register_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setHomeActionContentDescription("로그인 화면 이동")
            setDisplayShowTitleEnabled(false)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.back_front_button_menu, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
//            R.id.forward_button -> {
//                if (!button_continue.isEnabled) {
//                    return false
//                } else {
//                    button_continue.performClick()
//                }
//            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    private fun addTextChangeListener() {

        // 이메일
        email_input_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyEmail(s.toString())) {
                    email_input_layout.error = resources.getString(R.string.id_input_error)
                    check_email_button.isEnabled = false
                } else {
                    email_input_layout.error = null
                    email_input_layout.hint = null
                }

                check_email_button.isEnabled =
                    email_input_layout.error == null

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


    }

    private fun showSettingPopup() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.check_email_dialog, null)
        val emailCodeCheckEditText: EditText = view.findViewById(R.id.email_code_check_edit_text)
        val emailCheckTimer: TextView = view.findViewById(R.id.email_check_timer)
        val checkEmailButton2 = view.findViewById<Button>(R.id.checkEmailButton2)
        val conversionTime = "000500" // 5분 타이머

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("이메일 인증번호 확인")
            .create()

        // 카운트 다운 시작
        countDown(conversionTime,emailCheckTimer,alertDialog)

        checkEmailButton2.setOnClickListener {
            if (emailCodeCheckEditText.text.toString() == emailCode) {
                Toasty.normal(this, "인증번호가 확인되었습니다.", Toasty.LENGTH_SHORT)
                val intent = Intent(this, CustomerSignUp2::class.java)
                intent.putExtra("USER_EMAIL", email_input_edit_text.text.toString())
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                alertDialog.dismiss()
            } else {
                Toasty.normal(this, "인증번호를 다시 확인해주세요.", Toasty.LENGTH_SHORT)
            }

        }

        alertDialog.setView(view)
        alertDialog.show()
    }


}

