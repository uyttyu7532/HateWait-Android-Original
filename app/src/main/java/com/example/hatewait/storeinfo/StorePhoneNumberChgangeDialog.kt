package com.example.hatewait.storeinfo

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_store_info_update.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class StorePhoneNumberChgangeDialog : DialogFragment() {
    var customView: View? = null
    private lateinit var dialogListener: DialogListener

    val storePhoneLayout: TextInputLayout by lazy {
        customView?.findViewById<TextInputLayout>(R.id.store_phone_layout)!!
    }
    private val storePhoneEditText: TextInputEditText by lazy {
        customView?.findViewById<TextInputEditText>(R.id.store_phone_editText)!!
    }

    interface DialogListener {
        fun applyPhoneNumber(storePhoneNumber: String): Unit
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            customView = inflater.inflate(R.layout.fragment_store_phone_change_dialog, null)
            builder.setView(customView)
                .setTitle("가게 전화번호 수정")
                .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, _ ->
                    dismiss()
                })
                .setPositiveButton("변경", DialogInterface.OnClickListener { dialog, _ ->
                    dialogListener.applyPhoneNumber(storePhoneEditText.text.toString())
                })
            builder.create()
        } ?: throw IllegalStateException("Activity Can't be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogListener = context as DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement Store Phone Number Change Listener"))
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return customView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroyView() {
        customView = null
        super.onDestroyView()
    }

    private fun init() {
        val phoneRegex = Regex("^[0](\\d{2})(\\d{3,4})(\\d{3,4})")

//        추후 PhoneNumberTextWatcher() 추가 고려
//        Auto Hyphen로 유저 배려
//        or Spinner를 통해 지역번호 선택할 수 있게
        storePhoneEditText.setText(activity?.store_phone_number_textView?.text)
        storePhoneEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(storePhoneNumber: Editable?) {
                if (!storePhoneNumber.toString().matches(phoneRegex)) {
                    storePhoneLayout.error = resources.getString(R.string.store_phone_error_message)
                } else {
                    storePhoneLayout.error = null
                    storePhoneLayout.hint = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}