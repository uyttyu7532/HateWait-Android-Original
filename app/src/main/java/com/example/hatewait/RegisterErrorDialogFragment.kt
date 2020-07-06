package com.example.hatewait


import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_register_error_dialog.*

class RegisterErrorDialogFragment : DialogFragment() {

    var customView : View? = null
    lateinit var dialogListener : RegisterMemberDialogListener
    private val customerIdEditText : TextInputEditText by lazy {
        customView?.findViewById<TextInputEditText>(R.id.member_id_editText)!!
    }
    private val customerIdLayout : TextInputLayout by lazy {
        customView?.findViewById<TextInputLayout>(R.id.member_id_layout)!!
    }


    interface RegisterMemberDialogListener {
        fun applyText(memberId : String) : Unit
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            customView = inflater.inflate(R.layout.fragment_register_error_dialog, null)
            builder.setView(customView)
                .setTitle("회원 정보 오류")
                .setIcon(resources.getDrawable(R.drawable.main_logo, context?.theme))
                .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, _ ->
                    dismiss()
                })
                .setPositiveButton("변경",DialogInterface.OnClickListener { dialog, _ ->
                    dialogListener.applyText(customerIdEditText.text.toString())
                })
            val dialog = builder.create()
//            커스터마이징 끝 return은 커스터마이징 된 dialog
            dialog
//           if builder fails to create Activity ->  Elvis Operator null check
        } ?: throw IllegalStateException("Activity Can't be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogListener = context as RegisterMemberDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement dialogListener"))
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
        val idRegex = Regex("^(?=.*[a-zA-Zㄱ-ㅎ가-힣0-9])[a-zA-Zㄱ-ㅎ가-힣0-9]{1,}$")
        fun verifyId(input_id : String) : Boolean = idRegex.matches(input_id)

        customerIdEditText.text = activity?.findViewById<TextInputEditText>(R.id.user_id_input_editText)?.text

        customerIdEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyId(s.toString())) {
                    customerIdLayout.error = "특수문자나 공백은 허용되지 않습니다."
                } else {
                    customerIdLayout.error = null
                    customerIdLayout.hint = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun onDestroyView() {
        customView = null
        Log.i("onDestroyView", "this is dialog destroyed!!! without memory leak")
        super.onDestroyView()
    }
}