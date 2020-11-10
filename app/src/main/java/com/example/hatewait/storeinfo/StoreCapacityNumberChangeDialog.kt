package com.example.hatewait.storeinfo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.hatewait.R
import com.example.hatewait.login.storeInfo
import com.example.hatewait.retrofit2.MyApi
import kotlinx.android.synthetic.main.activity_store_info_update2.*
import kotlinx.android.synthetic.main.activity_store_introduce_change_dialog.*
import kotlinx.android.synthetic.main.fragmet_store_capacity_change_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreCapacityNumberChangeDialog : DialogFragment() {
    var customView: View? = null
    private lateinit var dialogListener: DialogListener


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
                    val updatedStoreCapacity = number_picker_capacity.value
                    dialogListener.applyCapacityNumber(updatedStoreCapacity.toString())
                    MyApi.UpdateService.requestStoreCapacityUpdate(
                        id = storeInfo!!.id,
                        maximum_capacity = updatedStoreCapacity
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

    }
}