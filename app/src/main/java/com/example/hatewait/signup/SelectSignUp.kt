package com.example.hatewait.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_select_sign_up.*
import org.jetbrains.anko.startActivity

class SelectSignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_sign_up)


        // TODO 네이버 회원가입으로 진입 구분

        store_sign_up_button.setOnClickListener{
            startActivity<StoreSignUp1>()
        }

        customer_sign_up_button.setOnClickListener{
            startActivity<CustomerSignUp1>()
        }
    }
}