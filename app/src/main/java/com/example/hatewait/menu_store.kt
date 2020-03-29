package com.example.hatewait

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_menu_store.*

class menu_store : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_store)

       idView.text=intent.getStringExtra("id").toString()

        val ListMenu_store= resources.getStringArray(R.array.menu_store)

        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ListMenu_store)
        listView.adapter = adapter

        listView.setOnItemClickListener { adapterView, view, position, I ->
            idView.text = ListMenu_store[position]
            if(position==0){
                Toast.makeText(this,"첫번째 클릭",Toast.LENGTH_SHORT).show()
            }
            if(position==1){
                Toast.makeText(this,"두번째 클릭",Toast.LENGTH_SHORT).show()
            }
        }
    }
}

