package com.example.hatewait

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.store_info.*

class StoreInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_info)

        titleView.text=intent.getStringExtra("menu_name").toString()
    }
}
