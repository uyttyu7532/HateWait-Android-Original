package com.example.hatewait.storeinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_store_info_update2.*
import org.jetbrains.anko.startActivity

class ChangePasswordActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        setSupportActionBar(register_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setDisplayShowTitleEnabled(false)
        }

        button_continue.setOnClickListener{
            // TODO 현재 비밀번호가 맞다면 다음 페이지로 넘어가게
            startActivity<ChangePasswordActivity2>()
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
}