package com.example.hatewait.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hatewait.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class MapBottomSheet : BottomSheetDialogFragment() {

    var data: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        data = arguments?.getString("mapPOIItem")
        Log.d("마커에서 전달된 내용 : ", data)

        return inflater.inflate(R.layout.map_expanding_bottom_sheet, container, false)
    }

    override fun getTheme(): Int {
        return super.getTheme()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //가게이름,전화번호,주소,영업시간,가게소개,대기인원

        view?.findViewById<TextView>(R.id.bottom_sheet_store_name)!!.text =
            data.toString().split(",")[0]
        view?.findViewById<TextView>(R.id.bottom_sheet_store_phone_num)!!.text =
            data.toString().split(",")[1]
        view?.findViewById<TextView>(R.id.bottom_sheet_store_address)!!.text =
            data.toString().split(",")[2]
        view?.findViewById<TextView>(R.id.bottom_sheet_store_business_time)!!.text =
            data.toString().split(",")[3]
        view?.findViewById<TextView>(R.id.bottom_sheet_store_description)!!.text =
            data.toString().split(",")[4]
        view?.findViewById<TextView>(R.id.bottom_sheet_store_waiting_num)!!.text =
            data.toString().split(",")[5]+"명"





//        data.toString().split(",")[0]
//        data.toString().split(",")[2]

        view?.findViewById<MaterialButton>(R.id.button_bottom_sheet)?.setOnClickListener {
            dismiss()
        }
    }
}