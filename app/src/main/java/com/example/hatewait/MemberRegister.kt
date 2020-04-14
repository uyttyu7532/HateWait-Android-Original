package com.example.hatewait

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_members_register.*
import org.jetbrains.anko.support.v4.startActivity

class MemberRegister : Fragment() {

    //   fragment 안에서 옵션 선택을 가능하게함.
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View ? {
        return inflater.inflate(R.layout.activity_members_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        user_id_form.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val str = s.toString()
                register_customer_button.isEnabled = str.isNotEmpty()
            }
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        register_customer_button.setOnClickListener {
//            등록이 끝났다면 지워줌!
            Toast.makeText(context, "등록되었어요!", Toast.LENGTH_SHORT).show()
            startActivity<Register_Check>(
                "USER_ID" to user_id_form.toString()
            )
            user_id_form.text.clear()
        }
        super.onActivityCreated(savedInstanceState)
    }

}