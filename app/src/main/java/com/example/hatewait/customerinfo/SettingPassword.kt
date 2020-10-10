package com.example.hatewait.customerinfo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.example.hatewait.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_customer_info_update.*
import kotlinx.android.synthetic.main.activity_customer_info_update.register_toolbar_title_textView
import kotlinx.android.synthetic.main.activity_setting_password.*
import org.jetbrains.anko.startActivity

private lateinit var mcontext: Context

class SettingPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_password)
        mcontext = this.applicationContext

        register_toolbar_title_textView.text = "비밀번호 변경"
        button_continue.text = "비밀번호 변경"

        button_continue.setOnClickListener {
            // TODO 현재 비밀번호가 맞는지 확인 => 아니라면 다음으로 못넘어가야함
            startActivity<SettingPassword2>()
            this.finish()
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

}