package com.example.hatewait


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_non_members_reigster.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.zip.Inflater

class NonMemberRegister : androidx.fragment.app.Fragment() {

// non_member 페이지를 열어줌
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View ? {
        return inflater.inflate(R.layout.activity_non_members_reigster, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        var user_name_not_empty = false
        var user_phone_number_not_empty = false

        user_name.addTextChangedListener(object : TextWatcher {
            //            text에 변화가 있을 때마다
            override fun afterTextChanged(p: Editable?) {
                val str = p.toString()
//    공백이 아닐 때 활성화시킴.
                user_name_not_empty = str.isNotEmpty()
                if(user_name_not_empty and user_phone_number_not_empty) register_customer_button.isEnabled = true
                else register_customer_button.isEnabled = false
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        user_phone_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val str = s.toString()
                user_phone_number_not_empty = str.isNotEmpty()
                if(user_name_not_empty and user_phone_number_not_empty) register_customer_button.isEnabled = true
                else register_customer_button.isEnabled = false
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        register_customer_button.setOnClickListener{
//            둘다 입력되어있으면 code flow는 첫줄에서 반환됨.
            if(user_name_not_empty and user_phone_number_not_empty) {
                Toast.makeText(context, "등록되었습니다!", Toast.LENGTH_SHORT).show()
                startActivity<Register_Check>(
                    "USER_NAME" to user_name.text.toString(),
                    "USER_PHONE_NUMBER" to user_phone_number.toString()
                )
                user_name.text.clear()
                user_phone_number.text.clear()
            }
            else if(user_phone_number_not_empty) Toast.makeText(context, "이름를 입력해주세요!", Toast.LENGTH_SHORT).show()
            else if(user_name_not_empty) Toast.makeText(context, "전화번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
//            else문에 도달할 일은 없음 (둘다 empty 인 경우 버튼 상태가 disabled 이기 때문에)
            else {}
        }

        super.onActivityCreated(savedInstanceState)
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


