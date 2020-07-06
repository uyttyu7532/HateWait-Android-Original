package com.example.hatewait

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_store_info_update.*
import org.jetbrains.anko.layoutInflater
import java.lang.ClassCastException

class StoreNameChangeDialog : AppCompatDialogFragment() {
    // 그대로 두면 onCreateView method에서 null을 리턴하면서
    //     onActivityCreated, onViewCreated 가 모두 호출되지 않음
    // viewModel 을 커스터마이징 할 수 가없으므로 일종의 트릭을 건다.
    var customView : View? = null
    lateinit var dialogListener : DialogListener
    private val storeNameRegex = Regex("^(?=.*[a-zA-Z가-힣0-9])[a-zA-Z가-힣0-9|\\s|,]{1,}$")
    fun verifyName(storeName : String) : Boolean = storeNameRegex.matches(storeName)

    interface DialogListener {
        fun applyText(storeName : String) : Unit
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
//        val inflater : LayoutInflater = activity?.layoutInflater!!
        customView = context?.layoutInflater?.inflate(R.layout.activity_store_name_change_dialog, null)!!
        val editStoreNameText : TextInputEditText = customView?.findViewById(R.id.store_name_editText)!!

        builder.setView(customView)
            .setTitle("가게이름")
            .setNegativeButton("취소",DialogInterface.OnClickListener { dialogInterface, _ ->
                dismiss()
            })
            .setPositiveButton("변경", DialogInterface.OnClickListener { dialogInterface, _ ->
                val updatedStoreName = editStoreNameText.text.toString()
                dialogListener.applyText(updatedStoreName)
            })
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            dialogListener = context as DialogListener

        } catch (e : ClassCastException) {
            throw ClassCastException((context.toString() + "must implement Store Name Change Listener"))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("onCreateView", "this is dialog onCreateView")
        return customView
    }

//    호출안됨

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i("onActivityCreated", "this is dialog onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }
// 호출안됨 -> customView를 onCreateView에서 리턴함으로써 호출됨.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("onViewCreated", "this is dialog initialize")
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroyView() {
//        Memory Leak을 방지하기 위함
        customView = null
        Log.i("onDestroyView", "this is dialog destroyed!!! without memory leak")
        super.onDestroyView()
    }

    private fun init() {
        val editStoreNameText : TextInputEditText = customView?.findViewById(R.id.store_name_editText)!!
        editStoreNameText.setText(activity?.store_name?.text)
        val editStoreNameLayout : TextInputLayout = customView?.findViewById(R.id.store_name_text_layout)!!
        editStoreNameText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(str: Editable?) {
                if (!verifyName(str.toString())) {
                    editStoreNameLayout.error = "특수문자는 허용되지 않습니다."
                } else {
                    editStoreNameLayout.error = null
                    editStoreNameLayout.hint = null
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }
}
