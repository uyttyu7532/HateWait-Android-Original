package com.example.hatewait.register

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_name_check_dialog.*


class NameCheckDialogFragment : DialogFragment() {

    private lateinit var nameChecklistener: NameCheckListener
    private var customView: View? = null

    interface NameCheckListener {
        fun onDialogPositiveClick(dialog: DialogFragment, customer_id:String, customer_people_num:Integer )
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    private var markedCustomerName = "문X훈"
    private var customer_id = ""
    private var customer_people_num:Int = 0

    //    롤리팝 이하버전은 지원 X
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


//    argument를 얻는 시점은 onCreateDialog (onCreate가 끝난 이후)
        markedCustomerName = markCustomerName(arguments?.getString("CUSTOMER_NAME")!!)
        customer_id = arguments?.getString("CUSTOMER_ID").toString()
        customer_people_num = arguments?.getString("CUSTOMER_PEOPLE_NUM")!!.toInt()


        //                .setPositiveButtonIcon(R.drawable)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            customView = inflater.inflate(R.layout.activity_name_check_dialog, null)
            builder.setView(customView)
            builder.setTitle("회원 이름 확인")
//                .setIcon(resources.getDrawable(R.drawable.main_logo, context!!.theme))
                .setMessage("$markedCustomerName 님 이신가요?")
            val dialog = builder.create()
//            커스터마이징 끝 return은 커스터마이징 된 dialog
            dialog
//           if builder fails to create Activity ->  Elvis Operator null check
        } ?: throw IllegalStateException("Activity Can't be null")
    }


    override fun onResume() {
        val width = resources.getDimensionPixelSize(R.dimen.dialog_fragment_width)
        val height = resources.getDimensionPixelSize(R.dimen.dialog_fragment_height)

        dialog?.window?.setLayout(width, height)
        super.onResume()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            nameChecklistener = context as NameCheckListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement NameCheckListener"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return customView
    }

    //onCreateView에서 넘겨준 customView를 넘겨받음.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name_yes_button.setOnClickListener {
            Log.i("test", "이게 된다고?")
            nameChecklistener.onDialogPositiveClick(this,customer_id, Integer(customer_people_num))
        }
        name_no_button.setOnClickListener {
            Log.i("test", "이게 안된다고?")
            nameChecklistener.onDialogNegativeClick(this)
        }

    }

    override fun onDestroyView() {
        customView = null
        Log.i("onDestroyView", "this is dialog destroyed!!! without memory leak")
        super.onDestroyView()
    }

    //    이름 모자이크 함수
    private fun markCustomerName(customerName: String): String {
        //    한글/ 영어 구분해야함
        //    한글 -> 2~4자 -> 2~3글자 : 가운데 1  4글자: 2글자 모자이크
        //    영어 -> Last Name or First Name 모자이크
        //    한글 2~4자 (공백 허용 X) or 영문 First name 2~10, Last name 2~10
        val koreanNameRegex = Regex("^[가-힣]{2,4}$")
        val englishNameRegex = Regex("^[a-zA-Z]{2,10}|\\s[a-zA-Z]{2,10}\$")
        val nameLength = customerName.length
        lateinit var resultName: String

        resultName =
            if (customerName.matches(koreanNameRegex)) {
                if (nameLength <= 3) {
                    //                가운데 글자만 모자이크 처리
//                    [start Index, end Index)
                    customerName.replaceRange(nameLength / 2, nameLength / 2 + 1, "X")
                } else {
                    //                4자 이름인 경우 가운데 2글자 모자이크 처리
                    customerName.replaceRange(nameLength / 2, nameLength, "X")
                }
            } else {
                //            LastName 모자이크 처리
                var markX = ""
//          indices : index 를 취하고 싶을 때 사용 ==  i가 index property 를 가져옴.
                for (i in customerName.split(" ")[1].indices) {
                    markX += "x"
                }
                customerName.replaceAfter(" ", markX)
            }

        return resultName
    }
}