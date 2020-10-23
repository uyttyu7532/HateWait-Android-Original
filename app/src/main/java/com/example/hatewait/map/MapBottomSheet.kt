package com.example.hatewait.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hatewait.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class MapBottomSheet : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        Log.d("data : ", arguments?.getString("mapPOIItem"))

        return inflater.inflate(R.layout.map_expanding_bottom_sheet, container, false)
    }

    override fun getTheme(): Int {
        return super.getTheme()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<MaterialButton>(R.id.button_bottom_sheet)?.setOnClickListener {
            dismiss()
        }
    }
}