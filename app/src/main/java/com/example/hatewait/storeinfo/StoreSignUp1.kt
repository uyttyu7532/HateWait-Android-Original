package com.example.hatewait.storeinfo

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R
import com.example.hatewait.mail.SendMail
import com.example.hatewait.mail.emailCode
import kotlinx.android.synthetic.main.activity_store_register1.*


// 1단계 이메일 , 인증번호 (네아로면 생략)
// 2단계 비번, 비번확인
// 3단계 가게이름, 전화번호, 도로명주소
// 4단계 가게 영업시간, 인원 수, 문구
// 가입완료 환영 메시지 액티비티 or 로그인바로됨


private lateinit var mcontext: Context
private lateinit var senderTo: String

class StoreSignUp1 : AppCompatActivity() {

    var customView: View? = null
    private val idRegex = Regex(
        ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    )

    fun verifyId(input_id: String): Boolean = idRegex.matches(input_id)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_register1)
        mcontext = this.applicationContext
        addTextChangeListener()
        checkEmailButton.setOnClickListener {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.back_front_button_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

//        when (item.itemId) {
//            R.id.forward_button -> {
//                if (!button_continue.isEnabled) {
//                    return false
//                } else {
//                    button_continue.performClick()
//                }
//            }
//            android.R.id.home -> {
//                onBackPressed()
//            }
//        }
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
        countDown(conversionTime,emailCheckTimer,alertDialog)

        checkEmailButton2.setOnClickListener {
            if (idCheckEditText.text.toString() == emailCode) {
                Toast.makeText(this, "인증번호가 확인되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, StoreSignUp2::class.java)
                intent.putExtra("USER_ID", id_input_editText.text.toString())
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "인증번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            }

        }

        alertDialog.setView(view)
        alertDialog.show()
    }



    private fun countDown(time: String, checkTimer:TextView, dialog:Dialog) {
        var conversionTime: Long = 0

        // 1000 단위가 1초
        // 60000 단위가 1분
        // 60000 * 3600 = 1시간
        var getHour = time.substring(0, 2)
        var getMin = time.substring(2, 4)
        var getSecond = time.substring(4, 6)

        // "00"이 아니고, 첫번째 자리가 0 이면 제거
        if (getHour.substring(0, 1) === "0") {
            getHour = getHour.substring(1, 2)
        }
        if (getMin.substring(0, 1) === "0") {
            getMin = getMin.substring(1, 2)
        }
        if (getSecond.substring(0, 1) === "0") {
            getSecond = getSecond.substring(1, 2)
        }

        // 변환시간
        conversionTime =
            java.lang.Long.valueOf(getHour) * 1000 * 3600 + java.lang.Long.valueOf(getMin) * 60 * 1000 + java.lang.Long.valueOf(
                getSecond
            ) * 1000

        // 첫번쨰 인자 : 원하는 시간 (예를들어 30초면 30 x 1000(주기))
        // 두번쨰 인자 : 주기( 1000 = 1초)
        object : CountDownTimer(conversionTime, 1000) {
            // 특정 시간마다 뷰 변경
            override fun onTick(millisUntilFinished: Long) {

                // 시간단위
                var hour = (millisUntilFinished / (60 * 60 * 1000)).toString()

                // 분단위
                val getMin = millisUntilFinished - millisUntilFinished / (60 * 60 * 1000)
                var min = (getMin / (60 * 1000)).toString() // 몫

                // 초단위
                var second = (getMin % (60 * 1000) / 1000).toString() // 나머지

                // 밀리세컨드 단위
                val millis = (getMin % (60 * 1000) % 1000).toString() // 몫

                // 시간이 한자리면 0을 붙인다
                if (hour.length == 1) {
                    hour = "0$hour"
                }

                // 분이 한자리면 0을 붙인다
                if (min.length == 1) {
                    min = "0$min"
                }

                // 초가 한자리면 0을 붙인다
                if (second.length == 1) {
                    second = "0$second"
                }
                checkTimer.setText("$hour:$min:$second")
            }

            override fun onFinish() {
                Toast.makeText(mcontext, "인증시간이 종료되었습니다..", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }.start()
    }


}

