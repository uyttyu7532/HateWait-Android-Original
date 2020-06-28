package com.example.hatewait

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
import kotlinx.android.synthetic.main.activity_name_check_dialog.*


class NameCheckDialogFragment : DialogFragment() {

    internal lateinit var nameChecklistener : NameCheckListener
    private var customView : View? = null
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
            val inflater = requireActivity().layoutInflater
            customView = inflater.inflate(R.layout.activity_name_check_dialog, null)
            builder.setView(customView)
            builder.setTitle("회원 이름 확인")
//                .setIcon(resources.getDrawable(R.drawable.main_logo, context!!.theme))
                .setMessage("문X훈 회원님 맞나요?")

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
            nameChecklistener.onDialogPositiveClick(this)
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
}