package com.example.hatewait

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.store_menu.*
import org.jetbrains.anko.startActivity

class StoreMenu_grid : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_menu)

        newStoreinfo.setOnClickListener {
            Toast.makeText(this,"클릭!", Toast.LENGTH_SHORT).show()
        }

        checkWaiting.setOnClickListener {
            startActivity<JoinActivity>()
        }

    }
}

