package com.example.hatewait.member

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hatewait.R
import kotlinx.android.synthetic.main.fragment_stamp.*
import kotlinx.android.synthetic.main.fragment_stamp.view.*

class StampFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var bundle:Bundle = this.requireArguments()
        var storeId = bundle.getString("store_id")
        var stampCount = bundle.getInt("stamp_count")
        var maximumStamp = bundle.getInt("maximum_stamp")

        stamp_pie_view.setStepCountText("${stampCount}/${maximumStamp}")
        stamp_pie_view.setPercentage(stampCount/maximumStamp*360)
        stamp_description.text = "쿠폰 발행까지 \n${maximumStamp-stampCount}번 남았습니다."


        Log.d("retrofit2", "$stampCount $maximumStamp")

        return inflater.inflate(R.layout.fragment_stamp, container, false)
    }



}