package com.example.hatewait

import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.hatewait.socket.PORT
import com.example.hatewait.socket.SERVERIP
import kotlinx.android.synthetic.main.activity_members_register.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


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
            val userId = user_id_input_editText.text.toString()
            val numOfGroup =  people_number_editText.text.toString()
//            MemberRegisterAsyncTask(this@MemberRegister).execute(userId, numOfGroup)
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

    class MemberRegisterAsyncTask(context: MemberRegister) : AsyncTask<String, Unit, Unit>() {

        private lateinit var clientSocket: Socket
        private lateinit var reader: BufferedReader
        private lateinit var writer: PrintWriter


        override fun doInBackground(vararg params: String) { // 소켓 연결
            val storeId= "s0000"
            val userId = params[0]
            val numOfGroup = params[1]
            try {
                clientSocket = Socket(SERVERIP, PORT)
                writer = PrintWriter(clientSocket!!.getOutputStream(), true)
                reader = BufferedReader(InputStreamReader(clientSocket!!.getInputStream(), "UTF-8"))
                writer!!.println("INSQUE;MEMBER;$storeId;$userId;$numOfGroup")
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            } finally {
                writer.close()
                reader.close()
                clientSocket.close()
            }

        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
        }

    }

}