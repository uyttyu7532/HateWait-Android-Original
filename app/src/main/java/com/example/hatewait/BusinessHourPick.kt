package com.example.hatewait

import android.app.admin.SystemUpdatePolicy
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bashizip.bhlib.BusinessHours
import kotlinx.android.synthetic.main.activity_business_hour_pick.*
import java.io.InvalidObjectException
import java.io.Serializable

class BusinessHourPick : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_hour_pick)

        init()
    }

    fun init() {
        cancel_button.setOnClickListener {
            finish()
        }

        btn_apply.setOnClickListener { view: View? ->
            var bhs: List<BusinessHours?>? = null
            bhs = try {
                bh_picker.businessHoursList
            } catch (e: InvalidObjectException) {
//                여기 뭔가 잘못됨..아
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
//            val intent = Intent(this, ViewerActivity::class.java)
//            intent.putExtra(MainActivity.BH_LIST, bhs as Serializable?)
//            startActivity(intent)
        }
    }
}
