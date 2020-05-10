package com.example.hatewait

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.activity_store_info_update.*
import kotlinx.android.synthetic.main.activity_store_name_change_dialog.*
import java.lang.ClassCastException

class StoreNameChangeDialog : AppCompatDialogFragment() {
    lateinit var dialogListener : DialogListener
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater : LayoutInflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_store_name_change_dialog, null)

        builder.setView(view)
            .setTitle("가게이름")
            .setNegativeButton("취소",DialogInterface.OnClickListener { dialogInterface, i ->

            } )
            .setPositiveButton("변경", DialogInterface.OnClickListener { dialogInterface, i ->
                store_name.text = store_name_editText.text.toString()
            })

        init()
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogListener = context as DialogListener
        } catch (e : ClassCastException) {
            e.printStackTrace()
        }
    }

    interface DialogListener {
        fun applyText(storeName : String) : Unit
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        super.onViewCreated(view, savedInstanceState)
    }

    fun init() {
        val storeNameRegex = Regex("^[a-zA-Zㄱ-힣0-9|\\s|,]{1,}$")
        fun verifyName(storeName : String) : Boolean = storeNameRegex.matches(storeName)

        store_name_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(str: Editable?) {
                if (!verifyName(str.toString())) {
                    store_name_text.error = "특수문자는 허용되지 않습니다."
                } else {
                    store_name_text.error = null
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }
}
