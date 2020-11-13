package com.example.hatewait.member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hatewait.R
import kotlinx.android.synthetic.main.fragment_stamp.*

class StampFragment : Fragment(){

    private var storeName : String? = null
    private var stampBenefit : String? = null
    private var stampCount : Int? = null
    private var maximumStamp : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var bundle:Bundle = this.requireArguments()
        storeName = bundle.getString("store_name")
        stampBenefit = bundle.getString("stamp_benefit")
        stampCount = bundle.getInt("stamp_count")
        maximumStamp = bundle.getInt("maximum_stamp")



        return inflater.inflate(R.layout.fragment_stamp, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        stamp_pie_view.setStepCountText("${stampCount}/${maximumStamp}")
        stamp_pie_view.setPercentage(stampCount!!*360/maximumStamp!!)
//        store_name_text_view.text = storeName
        stamp_benefit.text = stampBenefit
        stamp_description.text = "쿠폰 발행까지 \n${maximumStamp!!-stampCount!!}번 남았습니다."
    }



}