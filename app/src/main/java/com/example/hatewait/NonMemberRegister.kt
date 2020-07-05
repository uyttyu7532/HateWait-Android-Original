package com.example.hatewait


import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.hatewait.socket.PORT
import com.example.hatewait.socket.SERVERIP
import com.twilio.rest.api.v2010.account.sip.ipaccesscontrollist.IpAddress
import kotlinx.android.synthetic.main.activity_non_members_reigster.*
import org.jetbrains.anko.support.v4.startActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.ref.WeakReference
import java.net.Socket

class NonMemberRegister : androidx.fragment.app.Fragment() {

//    한글 2~4자 (공백 허용 X) or 영문 First name 2~10, Last name 2~10
    private val nameRegex = Regex("^[가-힣]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$")

//    3자리 - 3 or 4자리 - 4자리
//    첫자리는 반드시 0으로 시작.
    private val phoneRegex = Regex("^[0](\\d{2})(\\d{3,4})(\\d{4})")
//   첫자리는 반드시 0이 아닌 숫자 총 2자리까지 입력가능 (1~99 입력 가능)
    private val peopleNumberRegex = Regex("^[1-9](\\d?)")

    fun verifyName (input_name : String) : Boolean = input_name.matches(nameRegex)
    fun verifyPhoneNumber (input_phone_number : String) : Boolean = input_phone_number.matches(phoneRegex)
    fun verifyPeopleNumber (input_people_number : String) : Boolean = input_people_number.matches(peopleNumberRegex)

// non_member 페이지를 열어줌
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View ? {
        return inflater.inflate(R.layout.activity_non_members_reigster, container, false)
    }

    override fun onStop() {
        //   editText Form Initialize
        inputLayoutInitialize()
        super.onStop()
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
                    user_name_input_layout.hint = null
                }

            //    둘다 에러가 없을 때 등록 버튼 활성화
                register_customer_button.isEnabled =
                            (user_name_input_layout.error == null
                            && user_phone_number_layout.error == null
                            && people_number_layout.error == null
                            && !user_phone_number_editText.text.isNullOrBlank()
                            && !people_number_editText.text.isNullOrBlank())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        user_phone_number_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyPhoneNumber(s.toString())) {
                    user_phone_number_layout.error = "10~11자리 전화번호를 입력해주세요"
                    register_customer_button.isEnabled = false
                } else {
                    user_phone_number_layout.error = null
                    user_phone_number_layout.hint = null
                }
                register_customer_button.isEnabled =
                            (user_name_input_layout.error == null
                            && user_phone_number_layout.error == null
                            && people_number_layout.error == null
                            && !user_name_input_editText.text.isNullOrBlank()
                            && !people_number_editText.text.isNullOrBlank())
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
                            (user_name_input_layout.error == null
                            && user_phone_number_layout.error == null
                            && people_number_layout.error == null
                            && !user_name_input_editText.text.isNullOrBlank()
                            && !user_phone_number_editText.text.isNullOrBlank())
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


        register_customer_button.setOnClickListener{
//            둘다 입력되어있으면 code flow는 첫줄에서 반환됨.

            Toast.makeText(context, "등록되었습니다!", Toast.LENGTH_SHORT).show()
            NonMemberRegisterAsyncTask(this@NonMemberRegister)
                .execute(user_name_input_editText.text.toString()
                    , user_phone_number_editText.text.toString()
                    , people_number_editText.text.toString()
                    )
        }


        super.onActivityCreated(savedInstanceState)
    }

    private fun inputLayoutInitialize() {
        user_name_input_editText.text?.clear()
        user_name_input_editText.clearFocus()
        user_name_input_layout.error = null
        user_name_input_layout.hint = "이름을 입력해주세요"
        user_phone_number_editText.text?.clear()
        user_phone_number_editText.clearFocus()
        user_phone_number_layout.error = null
        user_phone_number_layout.hint = "전화번호를 입력해주세요"
        people_number_editText.text?.clear()
        people_number_editText.clearFocus()
        people_number_layout.error = null
        people_number_layout.hint = "총 몇 분이 오셨나요?"
    }

    class NonMemberRegisterAsyncTask(context: NonMemberRegister) : AsyncTask<String, Unit, String>() {
        val activityReference = WeakReference(context)
        private lateinit var clientSocket: Socket
        private lateinit var reader: BufferedReader
        private lateinit var writer: PrintWriter
        private var resultString = ""

        override fun doInBackground(vararg params: String) : String { // 소켓 연결
            val storeId= "s0000"
            val userName = params[0]
            val userPhone = params[1]
            val numOfGroup = params[2]
            try {
                clientSocket = Socket(SERVERIP, PORT)
                writer = PrintWriter(clientSocket.getOutputStream(), true)
                reader = BufferedReader(InputStreamReader(clientSocket.getInputStream(), "UTF-8"))
                writer.println("INSQUE;NONMEM;$storeId;$userName;$userPhone;$numOfGroup")
                resultString = reader.readLine()
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            } finally {
                writer.close()
                reader.close()
                clientSocket.close()
            }
//            try {
//
//                Log.i("nonMember", resultString)
//            } catch (ioe : IOException) {
//                ioe.printStackTrace()
//            } finally {
//
//            }
            return resultString
        }

        override fun onPostExecute(result: String) {
            val currentActivity = activityReference.get()
            super.onPostExecute(result)
//            server response string : INSQUE;NONMEM;22
//            마지막 delimeter ; 이후 '22'는 대기번호를 의미
            val customerTurnNumber = result.substringAfterLast(";").toInt()
            currentActivity?.startActivity<RegisterCheck>(
                "CUSTOMER_NAME" to currentActivity.user_name_input_editText.text.toString(),
                "CUSTOMER_TURN" to customerTurnNumber
            )
        }

    }
}


