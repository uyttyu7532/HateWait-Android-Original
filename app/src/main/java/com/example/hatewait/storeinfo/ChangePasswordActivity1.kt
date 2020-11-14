package com.example.hatewait.storeinfo

import LottieDialogFragment.Companion.fragment
import LottieDialogFragment.Companion.newInstance
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.hatewait.R
import com.example.hatewait.model.MemberLoginRequestData
import com.example.hatewait.model.MemberLoginResponseData
import com.example.hatewait.model.StoreLoginRequestData
import com.example.hatewait.model.StoreLoginResponseData
import com.example.hatewait.retrofit2.MyApi
import kotlinx.android.synthetic.main.activity_change_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

lateinit var _ChangePassword1: Activity

class ChangePasswordActivity1 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        _ChangePassword1 = this


        setSupportActionBar(register_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setDisplayShowTitleEnabled(false)
        }

        val userId = intent.getStringExtra("USER_ID")
        val isStore = intent.getBooleanExtra("IS_STORE", true)

        button_continue.setOnClickListener {
            if (isStore) {
                if (fragment == null || (!(fragment?.isAdded)!!)) {
                    newInstance().show(supportFragmentManager, "")
                }
                MyApi.LoginService.requestStoreLogin(
                    StoreLoginRequestData(
                        userId,
                        password_input_edit_text.text.toString()
                    )
                )
                    .enqueue(object : Callback<StoreLoginResponseData> {
                        override fun onFailure(call: Call<StoreLoginResponseData>, t: Throwable) {
                            Log.d("retrofit2 가게비번확인 :: ", "로그인연결실패 $t")
                        }

                        override fun onResponse(
                            call: Call<StoreLoginResponseData>,
                            response: Response<StoreLoginResponseData>
                        ) {
                            newInstance().dismiss()
                            Log.d(
                                "retrofit2 가게비번확인 ::",
                                response.code().toString() + response.body().toString()
                            )
                            when (response.code()) {
                                200 -> {
                                    Toast.makeText(
                                        _ChangePassword1,
                                        "현재 비밀번호가 확인되었습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent =
                                        Intent(_ChangePassword1, ChangePasswordActivity2::class.java)
                                    intent.putExtra("USER_ID", userId)
                                    intent.putExtra("IS_STORE", true)
                                    startActivity(intent)
                                }
                                409 -> {
                                    Toast.makeText(
                                        _ChangePassword1,
                                        "비밀번호를 확인해주세요.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                500 -> {
                                    Toast.makeText(_ChangePassword1, "서버 오류", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                    }
                    )
            } else { // 손님
                if (fragment == null || (!(fragment?.isAdded)!!)) {
                    newInstance().show(supportFragmentManager, "")
                }
                MyApi.LoginService.requestMemberLogin(
                    MemberLoginRequestData(
                        userId,
                        password_input_edit_text.text.toString()
                    )
                )
                    .enqueue(object : Callback<MemberLoginResponseData> {
                        override fun onFailure(call: Call<MemberLoginResponseData>, t: Throwable) {
                            Log.d("retrofit2 손님비번확인 :: ", "연결실패 $t")
                            Toast.makeText(_ChangePassword1, "서버 연결실패", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<MemberLoginResponseData>,
                            response: Response<MemberLoginResponseData>
                        ) {
                            newInstance().dismiss()
                            Log.d(
                                "retrofit2 손님비번확인 ::",
                                response.code().toString() + response.body().toString()
                            )
                            when (response.code()) {
                                200 -> {
                                    Toast.makeText(
                                        _ChangePassword1,
                                        "현재 비밀번호가 확인되었습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent =
                                        Intent(_ChangePassword1, ChangePasswordActivity2::class.java)
                                    intent.putExtra("USER_ID", userId)
                                    intent.putExtra("IS_STORE", false)
                                    startActivity(intent)
                                }
                                409 -> {
                                    Toast.makeText(
                                        _ChangePassword1,
                                        "비밀번호를 확인해주세요.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                500 -> {
                                    Toast.makeText(_ChangePassword1, "서버 오류", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                    }
                    )
            }


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