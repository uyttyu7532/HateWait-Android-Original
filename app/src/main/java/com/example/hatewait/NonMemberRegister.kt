package com.example.hatewait


import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_non_members_reigster.*
import org.jetbrains.anko.support.v4.startActivity

class NonMemberRegister : androidx.fragment.app.Fragment() {

//    한글 2~4자 (공백 허용 X) or 영문 First name 2~10, Last name 2~10
    private val nameRegex = Regex("^[가-힣]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$")

//    3자리 - 3 or 4자리 - 4자리
    private val phoneRegex = Regex("(\\d{3})(\\d{3,4})(\\d{4})")

    fun verifyName (input_name : String) : Boolean = input_name.matches(nameRegex)
    fun verifyPhoneNumber (input_phone_number : String) : Boolean = input_phone_number.matches(phoneRegex)

// non_member 페이지를 열어줌
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View ? {
        return inflater.inflate(R.layout.activity_non_members_reigster, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        var user_name_not_empty = false
        var user_phone_number_not_empty = false

        user_name_input_editText.addTextChangedListener(object : TextWatcher {
            //            text에 변화가 있을 때마다
            override fun afterTextChanged(s: Editable?) {
                if(!verifyName(s.toString())) {
                    user_name_input_layout.error = "2~4자 한글 또는 영문이름을 입력해주세요"
                    register_customer_button.isEnabled = false
                } else {
                    user_name_input_layout.error = null
                }

            //    둘다 에러가 없을 때 등록 버튼 활성화
                register_customer_button.isEnabled = (user_name_input_layout.error == null && user_phone_number_layout.error == null)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        user_phone_number_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyPhoneNumber(s.toString())) {
                    user_phone_number_layout.error = "11자리 전화번호를 입력해주세요"
                    register_customer_button.isEnabled = false
                } else {
                    user_phone_number_layout.error = null
                }
                register_customer_button.isEnabled = (user_name_input_layout.error == null && user_phone_number_layout.error == null)
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        register_customer_button.setOnClickListener{
//            둘다 입력되어있으면 code flow는 첫줄에서 반환됨.

                Toast.makeText(context, "등록되었습니다!", Toast.LENGTH_SHORT).show()
                startActivity<Register_Check>(
                    "USER_NAME" to user_name_input_editText.text.toString(),
                    "USER_PHONE_NUMBER" to user_phone_number_editText.toString()
                )
//           intent를 넘기고 초기화해야함.
            inputLayoutInitialize()

        }

        super.onActivityCreated(savedInstanceState)
    }

    fun inputLayoutInitialize() {
        user_name_input_editText.text?.clear()
        user_name_input_editText.clearFocus()
        user_name_input_layout.error = null
        user_phone_number_editText.text?.clear()
        user_phone_number_editText.clearFocus()
        user_phone_number_layout.error = null

    }

}


