package com.example.hatewait

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_members_register.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity

class MemberRegister : Fragment() {
    private val idRegex = Regex("^(?=.*[a-zA-Zㄱ-ㅎ가-힣0-9])[a-zA-Zㄱ-ㅎ가-힣0-9]{1,}$")
    private val peopleNumberRegex = Regex("^[1-9](\\d?)")

    fun verifyId(input_id : String) : Boolean = idRegex.matches(input_id)
    fun verifyPeopleNumber (input_people_number : String) : Boolean = input_people_number.matches(peopleNumberRegex)


    //   fragment 안에서 옵션 선택을 가능하게함.
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View ? {
        return inflater.inflate(R.layout.activity_members_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        user_id_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyId(s.toString())) {
                    user_id_input_layout.error = "특수문자나 공백은 허용되지 않습니다."
                    register_customer_button.isEnabled = false
                } else {
                    user_id_input_layout.error = null
                    user_id_input_layout.hint = null
                    register_customer_button.isEnabled =
                                (user_id_input_layout.error == null
                                && people_number_layout.error == null
                                && !people_number_editText.text.isNullOrBlank())
                }

            }
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


        people_number_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyPeopleNumber(s.toString())) {
                    people_number_layout.error = "단체 손님은 가게에 문의해주세요"
                    register_customer_button.isEnabled = false
                } else {
                    people_number_layout.error = null
                    people_number_layout.hint = null
                }
                register_customer_button.isEnabled =
                            (user_id_input_layout.error == null
                            && people_number_layout.error == null
                            && !user_id_input_editText.text.isNullOrBlank())
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        register_customer_button.setOnClickListener {
//            등록이 끝났다면 지워줌!
//            Toast.makeText(context, "등록되었어요!", Toast.LENGTH_SHORT).show()
//            DialogActivity (이름 3자중 가운데 모자이크 ex. 문X훈 회원님 맞으신가요?)
            showNameCheckDialog()
//            startActivity<RegisterCheck>(
//                "USER_ID" to user_id_input_editText.toString()
//            )
        }
        super.onActivityCreated(savedInstanceState)

    }

    override fun onStop() {
        inputLayoutInitialize()
        super.onStop()
    }

    private fun inputLayoutInitialize() {
        user_id_input_editText.error = null
        user_id_input_editText.clearFocus()
        user_id_input_editText.text?.clear()
        user_id_input_layout.hint = "헤잇웨잇의 아이디를 입력해주세요"
        people_number_layout.error = null
        people_number_editText.clearFocus()
        people_number_editText.text?.clear()
        people_number_layout.hint = "총 몇 분이 오셨나요?"
    }

    private fun showNameCheckDialog() {
        val nameCheckFragment = NameCheckDialogFragment()
        nameCheckFragment.show(activity!!.supportFragmentManager, "NameCheck")
    }

}