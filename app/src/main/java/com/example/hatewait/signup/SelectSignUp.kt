package com.example.hatewait.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_select_sign_up.*
import org.jetbrains.anko.startActivity

class SelectSignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_sign_up)

        var isFromNaver = intent.getBooleanExtra("isFromNaver",false)
        if(isFromNaver){
            var naverUserId = intent.getStringExtra("naverUserId")
            var naverUserEmail = intent.getStringExtra("naverUserEmail")
            var naverUserName = intent.getStringExtra("naverUserName")
            Toast.makeText(this, "$isFromNaver $naverUserId $naverUserEmail $naverUserName", Toast.LENGTH_SHORT ).show()

            store_sign_up_button.setOnClickListener{
                val intent = Intent(this, StoreSignUp3::class.java)
                intent.putExtra("isFromNaver", true)
                intent.putExtra("naverUserId", naverUserId)
                intent.putExtra("naverUserEmail", naverUserEmail)
                intent.putExtra("naverUserName", naverUserName)
                this.startActivity(intent)
            }

            customer_sign_up_button.setOnClickListener{
                val intent = Intent(this, CustomerSignUpWithNaver::class.java)
                intent.putExtra("isFromNaver", true)
                intent.putExtra("naverUserId", naverUserId)
                intent.putExtra("naverUserEmail", naverUserEmail)
                intent.putExtra("naverUserName", naverUserName)
                this.startActivity(intent)
            }

        }
        else{
            Toast.makeText(this, "$isFromNaver", Toast.LENGTH_SHORT ).show()
            store_sign_up_button.setOnClickListener{
                startActivity<StoreSignUp1>()
            }

            customer_sign_up_button.setOnClickListener{
                startActivity<CustomerSignUp1>()
            }
        }



    }
}