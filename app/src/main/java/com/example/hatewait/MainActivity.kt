package com.example.hatewait

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity



class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var id = "건대보쌈"
        var waitingnum = "10"

        button_store.setOnClickListener{
            startActivity<store_menu>(
            "storename" to id,
            "waitingnum" to waitingnum
            )
        }


        var customername = "조예린"

        button_customer.setOnClickListener{
            startActivity<customer_menu>(
                "customername" to customername
            )
        }





//        //현재 기기의 토큰을 가져와서 출력 해보자.
//        myToken()


    }

//    private fun myToken() =//쓰레드 사용할것
//        Thread(Runnable {
//            try {
//                FirebaseInstanceId.getInstance().instanceId
//                    .addOnCompleteListener(OnCompleteListener { task ->
//                        if (!task.isSuccessful) {
//                            Log.i(TAG, "getInstanceId failed", task.exception)
//                            return@OnCompleteListener
//                        }
//                        val token = task.result?.token
////                        textView.text = token
//                        //Log.d(TAG, token)
//                    })
//            } catch (e: IOException){
//                e.printStackTrace()
//            }
//        }).start()




}