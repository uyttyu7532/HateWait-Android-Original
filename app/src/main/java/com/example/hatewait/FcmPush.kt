package com.example.hatewait

import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


// TODO 포그라운드 수신
// TODO 사용자 토큰 저장

class FcmPush() {
    val JSON = MediaType.parse("application/json; charset=utf-8")//Post전송 JSON Type
    val url = "https://fcm.googleapis.com/fcm/send" //FCM HTTP를 호출하는 URL
    val serverKey =
        "AAAADFuBNLs:APA91bFIXgmQdl1BI8uySmcALicDNbrgQwD8Y576IyrRAKe-BdYZDLmon5J4BKeUxMYzcFEBay8cJ1ntNDnmZ7QKCX1z-ldyZJKkiQSk4qdm3sBXLfVvwoyFv6JSgA8oI12pdgyELc_d"

    //Firebase에서 복사한 서버키
    var okHttpClient: OkHttpClient
    var gson: Gson

    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
    }

    fun test(context: Context) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    //Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = token
                //Log.d(TAG, msg)
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            })
    }

    fun sendMessage(context: Context) {
        var token =
//            "c1cacBQUc2Q:APA91bFt3MUoTGGaapIjrDj1aFVe_R5zvBAuZNDrPG7VVMsED0IS-lDTwHbDxbSWGG7kfLTTPDMsR2JR_Hb1M0bHMUC3kHOmVw5QclUyympAbz2jAAJUU1oEHutcg4r3T63Cld35p-Lb"
            "fiARZ0G9lxs:APA91bENjxB-zasfoMSaD3cfUl-d5wWFS9E50NcuSv6c91WWDXxLJl5-SV_tDEu8aHP3AgR_gTPmQVhW_k6yW73wxd2aVK_bn2n1h-8e-27qp7OiN-qcIKOkJZk94Hwuvqfs_KaKZSRj"
        var pushDTO = PushDTO()
//        pushDTO.to = token                   //푸시토큰 세팅
        pushDTO.to = "/topics/weather" // 토픽넣어주기(호출하는폰번호)
        pushDTO.notification?.title = "title"  //푸시 타이틀 세팅
        pushDTO.notification?.body = "message" //푸시 메시지 세팅

        var body = RequestBody.create(JSON, gson?.toJson(pushDTO)!!)
        var request = Request
            .Builder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "key=" + serverKey)
            .url(url)       //푸시 URL 세팅
            .post(body) //pushDTO가 담긴 body 세팅
            .build()
        okHttpClient?.newCall(request)?.enqueue(object : Callback {
            //푸시 전송
            override fun onFailure(call: Call?, e: IOException?) {
            }

            override fun onResponse(call: Call?, response: Response?) {

            }
        })
    }

}
