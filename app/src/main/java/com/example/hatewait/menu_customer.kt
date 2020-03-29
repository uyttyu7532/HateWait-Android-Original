package com.example.hatewait

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_menu_store.*

class menu_customer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_customer)

        val ListMenu_customer= resources.getStringArray(R.array.menu_customer)

        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ListMenu_customer)
        listView.adapter = adapter

        listView.setOnItemClickListener { adapterView, view, i, I ->
            idView.text = ListMenu_customer[i]


        }

    }
}
