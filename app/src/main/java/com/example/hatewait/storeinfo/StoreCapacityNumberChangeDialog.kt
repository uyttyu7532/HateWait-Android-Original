package com.example.hatewait.storeinfo

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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.hatewait.R
import com.example.hatewait.retrofit2.MyApi
import kotlinx.android.synthetic.main.activity_store_info_update.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_store_info_update2.*
import kotlinx.android.synthetic.main.activity_store_name_change_dialog.*
import kotlinx.android.synthetic.main.fragmet_store_capacity_change_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreCapacityNumberChangeDialog : DialogFragment() {
    var customView: View? = null
    private lateinit var dialogListener: DialogListener


//    val storeCapacityLayout: TextInputLayout by lazy {
//        customView?.findViewById<TextInputLayout>(R.id.store_capacity_layout)!!
//    }
//    private val storeCapacityEditText: TextInputEditText by lazy {
//        customView?.findViewById<TextInputEditText>(R.id.store_capacity_editText)!!
//    }

    interface DialogListener {
        fun applyCapacityNumber(capacityNumber: String): Unit
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            customView = inflater.inflate(R.layout.fragmet_store_capacity_change_dialog, null)
            builder.setView(customView)
                .setTitle("가게 수용인원 수정")
                .setNegativeButton("취소") { _, _ ->
                    dismiss()
                }
                .setPositiveButton("변경") { _, _ ->
//                    dialogListener.applyCapacityNumber(setting_capacity_text_view.text.toString())
                    MyApi.UpdateService.requestStoreCapacityUpdate(
                        id = "hate2020",
                        maximum_capacity = number_picker_capacity.value
                    )
                        .enqueue(object : Callback<MyApi.onlyMessageResponseData> {
                            override fun onFailure(
                                call: Call<MyApi.onlyMessageResponseData>,
                                t: Throwable
                            ) {
                                Log.d("retrofit2 수용인원수정 :: ", "수용인원수정실패 $t")
                            }

                            override fun onResponse(
                                call: Call<MyApi.onlyMessageResponseData>,
                                response: Response<MyApi.onlyMessageResponseData>
                            ) {

                                var data: MyApi.onlyMessageResponseData? = response?.body()
                                Log.d(
                                    "retrofit2 수용인원수정 ::",
                                    response.code().toString() + response.body().toString()
                                )
                                when (response.code()) {
                                    200 -> {
                                    }
                                }
                            }
                        })
                }
            builder.create()
        } ?: throw IllegalStateException("Activity Can't be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogListener = context as DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement Store Capacity Number Change Listener"))
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
////        첫자리 0으로 시작 불가 총 4자리수까지 입력가능.
//        val storeCapacityRegex = Regex("[^0](\\d{0,3})")
//        storeCapacityEditText.text = activity?.store_capacity_number_textView?.text
//        storeCapacityEditText.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(storeCapacityNumber: Editable?) {
//                if (!storeCapacityNumber.toString().matches(storeCapacityRegex)) {
//                    storeCapacityLayout.error = "9999명까지 입력 가능합니다."
//                } else {
//                    storeCapacityLayout.error = null
//                    storeCapacityLayout.hint = null
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })
    }
}