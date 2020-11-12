package com.example.hatewait.mail

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import javax.mail.MessagingException
import javax.mail.SendFailedException

class SendMail : AppCompatActivity() {
    // 보내는 계정의 id
    var user = "hatewait2020@gmail.com"

    // 보내는 계정의 pw
    var password = "hatewait2020"

    fun sendSecurityCode(context: Context?,  sendTo: String?) {
        try {
            val gMailSender = GmailSender(user, password)
            gMailSender.sendMail("HateWait 회원가입 메일입니다.", "인증번호: ${emailCode}", sendTo!!)
            Toast.makeText(context!!, "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show()
        } catch (e: SendFailedException) {
            Toast.makeText(context!!, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
        } catch (e: MessagingException) {
            Log.v("err", e.toString())
            Toast.makeText(context!!, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}