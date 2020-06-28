package com.example.hatewait

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class NameCheckDialogFragment : DialogFragment() {

    internal lateinit var nameChecklistener : NameCheckListener

    interface NameCheckListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }
//    롤리팝 이하버전은 지원 X
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //                .setPositiveButtonIcon(R.drawable)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
//            val inflater = requireActivity().layoutInflater
//            builder.setView(inflater.inflate(R.layout.activity_name_check_dialog, null))
            builder.setTitle("회원 이름 확인")
//                .setIcon(resources.getDrawable(R.drawable.main_logo, context!!.theme))
                .setMessage("문X훈 회원님 맞나요?")
                .setPositiveButton("", DialogInterface.OnClickListener { dialog, _ ->
                    nameChecklistener.onDialogPositiveClick(this)
                })
                .setPositiveButtonIcon(resources.getDrawable(R.drawable.yes_button_vector, context!!.theme))
                .setNegativeButton("", DialogInterface.OnClickListener { dialog, _ ->
                    nameChecklistener.onDialogNegativeClick(this)
                })
                .setNegativeButtonIcon(resources.getDrawable(R.drawable.no_button_vector, context!!.theme))
            val dialog = builder.create()
            dialog.setOnShowListener(OnShowListener { dialog ->
//                val buttonHeight = resources.getDimensionPixelSize(R.dimen.no_button_width)
//                val buttonWidth = resources.getDimensionPixelSize(R.dimen.yes_button_width)

                val negativeButton =
                    (dialog as AlertDialog).getButton(DialogInterface.BUTTON_NEGATIVE)
                val positiveButton =
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                params.gravity = Gravity.CENTER
                negativeButton.apply {
                    layoutParams = params
                }
                positiveButton.apply {
                    layoutParams = params
                }
            })

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
}