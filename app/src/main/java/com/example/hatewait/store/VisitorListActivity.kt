package com.example.hatewait.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_visitor_list.*

class VisitorListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visitor_list)

        init()
    }

    private fun init(){
        visitor_view_pager.adapter = VisitorListFragAdapter(this)
    }








}
