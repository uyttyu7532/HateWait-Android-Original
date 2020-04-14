package com.example.hatewait

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.zip.Inflater

class NonMemberRegister : Fragment() {

// non_member 페이지를 열어줌
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View ? {
        return inflater.inflate(R.layout.activity_non_members_reigster, container, false)
    }
/*
//   fragment 안에서 옵션 선택을 가능하게함.
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
// inflate the menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.menu_login, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    // 회원 & 비회원 항목 (여러 항목중 1개만 selected state)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        get item id to handle item clicks
        val id = item!!.itemId
//        handle item clicks
        if(id == R.id.main_logo) {
            Toast.makeText(activity, "환경 설정!!", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
*/
}


