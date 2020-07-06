package com.example.hatewait

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bashizip.bhlib.BusinessHours
import com.bashizip.bhlib.ValdationException
import kotlinx.android.synthetic.main.activity_business_hour_pick.*
import java.io.Serializable

class BusinessHourPick : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_hour_pick)

        init()
    }

    fun init() {
        var bhs: List<BusinessHours>? = null
        cancel_button.setOnClickListener {
            finish()
        }

        btn_apply.setOnClickListener {
//            bhs: List<BusinessHours?>? = null
            bhs = try {
                bh_picker.businessHoursList
            } catch (e: ValdationException) {
//                on 상태에서 영업시간을 입력하지 않은경우
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
//            val intent =
//                Intent(this, MainActivity::class.java)
//            intent.putExtra("BUSINESS_HOUR_LIST", bhs as Serializable?)
//            startActivity(intent)
        }
    }
}
