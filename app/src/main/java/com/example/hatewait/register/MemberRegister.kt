package com.example.hatewait.register

import LottieDialogFragment.Companion.fragment
import LottieDialogFragment.Companion.newInstance
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hatewait.R
import com.example.hatewait.model.*
import com.example.hatewait.retrofit2.MyApi
import kotlinx.android.synthetic.main.activity_members_register.*
import kotlinx.android.synthetic.main.activity_members_register.people_number_editText
import kotlinx.android.synthetic.main.activity_members_register.people_number_layout
import kotlinx.android.synthetic.main.activity_members_register.register_customer_button
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberRegister : Fragment() {
    private val idRegex = Regex("^(?=.*[a-zA-Zㄱ-ㅎ가-힣0-9])[a-zA-Zㄱ-ㅎ가-힣0-9]{1,}$")
    private val peopleNumberRegex = Regex("^[1-9](\\d?)")

    fun verifyId(input_id: String): Boolean = idRegex.matches(input_id)
    fun verifyPeopleNumber(input_people_number: String): Boolean =
        input_people_number.matches(peopleNumberRegex)

    var customerName: String? = null
    var customerTurn: Int? = null

//    LoginRegisterViewPagerAdapter <->MemberRegister Communication?
//    Bundle? Interface Listener?

    private lateinit var customerInfoListener: CustomerInfoListener

    interface CustomerInfoListener {
        fun registerCustomer(memberRegister: MemberRegister)
    }

    //   fragment 안에서 옵션 선택을 가능하게함.
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_members_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        user_id_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyId(s.toString())) {
                    user_id_input_layout.error = resources.getString(R.string.id_input_error)
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
                    people_number_layout.error = "단체 손님은 가게에 문의해주세요."
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
            val userId = user_id_input_editText.text.toString()
            val numOfGroup = people_number_editText.text.toString()
//            MemberRegisterAsyncTask(this@MemberRegister).execute(userId, numOfGroup)

//            var memberRegisterData = MemberRegisterRequestData(userId, Integer(numOfGroup), true)
            if (fragment == null || (!(fragment?.isAdded)!!)) {
                newInstance().show(requireActivity().supportFragmentManager, "")
            }
                MyApi.RegisterService.requestCheckMemberId(CheckMemberIdRequestData(userId))
                .enqueue(object : Callback<CheckMemberIdResponseData> {
                    override fun onFailure(
                        call: Call<CheckMemberIdResponseData>,
                        t: Throwable
                    ) {

                        Log.d("retrofit2 회원 id 확인 :: ", "연결실패 $t")
                    }

                    override fun onResponse(
                        call: Call<CheckMemberIdResponseData>,
                        response: Response<CheckMemberIdResponseData>
                    ) {
                        newInstance().dismiss()
                        Log.d("retrofit2 회원 id 확인 ::",response.code().toString() + response.body().toString())

                        if (response.code() == 200) {

                            var data: CheckMemberIdResponseData? = response?.body() // 서버로부터 온 응답

                            showNameCheckDialog(data!!.memberName, userId, numOfGroup)

                        }
                        if (response.code() == 409) {
                            var data: CheckMemberIdResponseData? = response?.body() // 서버로부터 온 응답)
                            showMemberIdErrorDialog()
                        }
                    }
                }
                )
        }
        super.onActivityCreated(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            customerInfoListener = context as CustomerInfoListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement dialogListener"))
        }
    }

    override fun onStop() {
        inputLayoutInitialize()
        super.onStop()
    }

    private fun inputLayoutInitialize() {
        user_id_input_editText.text?.clear()
        user_id_input_editText.clearFocus()
        user_id_input_layout.error = null
        user_id_input_layout.hint = "헤잇웨잇의 아이디를 입력해주세요"

        people_number_editText.text?.clear()
        people_number_editText.clearFocus()
        people_number_layout.error = null
        people_number_layout.hint = "총 몇 분이 오셨나요?"
    }


    fun showNameCheckDialog(checkName:String, customerId:String, customerPeopleNum:String ) {
        val nameCheckFragment =
            NameCheckDialogFragment()
        val argumentBundle = Bundle()
        argumentBundle.putString("CUSTOMER_NAME", checkName)
        argumentBundle.putString("CUSTOMER_ID", customerId)
        argumentBundle.putString("CUSTOMER_PEOPLE_NUM", customerPeopleNum)
        nameCheckFragment.arguments = argumentBundle
        nameCheckFragment.show(requireActivity().supportFragmentManager, "MEMBER_NAME_CHECK")
    }

    fun showMemberIdErrorDialog() {
        val memberIdCheckFragment =
            RegisterErrorDialogFragment()
        memberIdCheckFragment.show(requireActivity().supportFragmentManager, "MEMBER_ID_CHECK")
    }
}