package com.example.hatewait.member


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R

class CouponFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView = inflater.inflate(R.layout.fragment_coupon, container, false)

        var recyclerView = rootView.findViewById(R.id.coupon_recycler_view) as RecyclerView


        var couponTitleArray = ArrayList<String>()
        couponTitleArray.add("적립")
        couponTitleArray.add("소멸")
        couponTitleArray.add("사용")
        couponTitleArray.add("사용")
        couponTitleArray.add("적립")
        couponTitleArray.add("적립")
        couponTitleArray.add("소멸")
        couponTitleArray.add("사용")
        couponTitleArray.add("사용")
        couponTitleArray.add("적립")

        var couponDateArray = ArrayList<String>()
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")



        recyclerView.setHasFixedSize(true);
        var adapter = StampViewAdapter(couponTitleArray, couponDateArray)
        recyclerView.layoutManager =  LinearLayoutManager(activity)
        recyclerView.adapter = adapter



        return rootView
    }

}