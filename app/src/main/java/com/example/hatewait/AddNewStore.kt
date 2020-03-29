package com.example.hatewait

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_new_store.*

class AddNewStore : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_store)

        titleView.text=intent.getStringExtra("menu_name").toString()
    }
}
