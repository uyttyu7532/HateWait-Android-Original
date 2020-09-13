package com.example.hatewait.storeinfo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_auto_call_number_change_dialog.*
import org.jetbrains.anko.layoutInflater
import java.lang.ClassCastException

class AutoCallNumberChangeDialog : AppCompatDialogFragment() {
    // 그대로 두면 onCreateView method에서 null을 리턴하면서
    //     onActivityCreated, onViewCreated 가 모두 호출되지 않음
    // viewModel 을 커스터마이징 할 수 가없으므로 일종의 트릭을 건다.
    var customView: View? = null
    lateinit var dialogListener: DialogListener

    interface DialogListener {
        fun applyPickedNumber(autoCallNumber: String): Unit
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
//        val inflater : LayoutInflater = activity?.layoutInflater!!
        customView = context?.layoutInflater?.inflate(
            R.layout.activity_auto_call_number_change_dialog,
            null
        )!!
        val numberPicker: NumberPicker = customView?.findViewById(R.id.number_picker)!!

        builder.setView(customView)
            .setTitle("자동 호출 팀 번호")
            .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->

            })
            .setPositiveButton("변경", DialogInterface.OnClickListener { dialogInterface, i ->
                val updatedAutoCallNumber = numberPicker.value
                dialogListener.applyPickedNumber(updatedAutoCallNumber.toString())
            })
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            dialogListener = context as DialogListener
        } catch (e: ClassCastException) {
            throw e
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
        number_picker.apply {
            minValue = 1
            maxValue = 20
        }
    }
}