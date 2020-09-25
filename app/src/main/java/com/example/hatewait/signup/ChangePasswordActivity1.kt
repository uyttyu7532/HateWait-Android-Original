package com.example.hatewait.signup

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


private lateinit var mcontext: Context
private lateinit var senderTo: String

class FindPassWordActivity : AppCompatActivity() {

    var customView: View? = null
    private val idRegex = Regex(
        ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    )

    fun verifyId(input_id: String): Boolean = idRegex.matches(input_id)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup1)
        mcontext = this.applicationContext

        register_toolbar_title_textView.text = "비밀번호 변경"


        addTextChangeListener()
        checkEmailButton.setOnClickListener {
            // TODO 디비에서 존재하는 아이디인지 확인 후

            //인터넷 사용권한 허가
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .permitDiskReads()
                    .permitDiskWrites()
                    .permitNetwork().build()
            )

            senderTo = id_input_editText.text.toString()
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

        // 아이디
        id_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyId(s.toString())) {
                    id_input_layout.error = resources.getString(R.string.id_input_error)
//                    button_continue.isEnabled = false
                } else {
                    id_input_layout.error = null
                    id_input_layout.hint = null
                }


                checkEmailButton.isEnabled =
                    id_input_layout.error == null

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
        val idCheckEditText: EditText = view.findViewById(R.id.id_check_editText)
        val emailCheckTimer: TextView = view.findViewById(R.id.email_check_timer)
        val checkEmailButton2 = view.findViewById<Button>(R.id.checkEmailButton2)
        val conversionTime = "000500" // 5분 타이머

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("이메일 인증번호 확인")
            .create()

        // 카운트 다운 시작
        countDown(conversionTime, emailCheckTimer, alertDialog)

        checkEmailButton2.setOnClickListener {
            if (idCheckEditText.text.toString() == emailCode) {
                Toasty.normal(mcontext, "인증번호가 확인되었습니다.", Toasty.LENGTH_SHORT)
                val intent = Intent(this, ChangePasswordActivity::class.java)
                intent.putExtra("USER_ID", id_input_editText.text.toString())
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                alertDialog.dismiss()
            } else {
                Toasty.normal(mcontext, "인증번호를 다시 확인해주세요.", Toasty.LENGTH_SHORT)
            }

        }

        alertDialog.setView(view)
        alertDialog.show()
    }


}

