package com.example.hatewait

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_store_info_update.*

class StoreInfoUpdate : AppCompatActivity() {
    fun openDialog() {
//            여기 아직 확실하지않음.
        val storeNameChangeDialog = StoreNameChangeDialog()
        storeNameChangeDialog.show(supportFragmentManager, "store name changing!")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_info_update)
        init()
    }

    fun init() {

        store_name_edit_button.setOnClickListener {
            openDialog()
        }
        store_business_hours_text.setOnClickListener {
            val intent = Intent(this@StoreInfoUpdate, BusinessHourPick::class.java)
            startActivity(intent)
        }

    }

}
