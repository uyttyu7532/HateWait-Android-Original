package com.example.hatewait

import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class BusinessHourCheckDialog : DialogFragment() {
    private lateinit var timeCheckListener : TimeCheckListener
    private var customView : View? = null
    interface TimeCheckListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {
        val updatedBusinessHours = arguments?.getString("NEW_BUSINESS_HOURS")

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("영업 시간 확인")
                .setIcon(resources.getDrawable(R.drawable.main_logo, context?.theme))
                .setMessage("$updatedBusinessHours\n으로 변경하시겠어요?")
                .setPositiveButton("변경", DialogInterface.OnClickListener { dialog, _ ->

                })
                .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, _ ->
                    dismiss()
                })
            val dialog = builder.create()
            dialog
        } ?: throw IllegalStateException("Activity Can't be null")
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        try {
//            timeCheckListener = context as TimeCheckListener
//        } catch (e: ClassCastException) {
//            throw java.lang.ClassCastException((context.toString() + "must implement TimeCheckListener"))
//        }
//    }


}