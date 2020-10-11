package com.example.hatewait.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R

class StampFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView = inflater.inflate(R.layout.fragment_stamp, container, false)

        var recyclerView = rootView.findViewById(R.id.stamp_recycler_view) as RecyclerView


        var stampTitleArray = ArrayList<String>()
        stampTitleArray.add("적립")
        stampTitleArray.add("소멸")
        stampTitleArray.add("사용")
        stampTitleArray.add("사용")
        stampTitleArray.add("적립")
        stampTitleArray.add("적립")
        stampTitleArray.add("소멸")
        stampTitleArray.add("사용")
        stampTitleArray.add("사용")
        stampTitleArray.add("적립")

        var stampDateArray = ArrayList<String>()
        stampDateArray.add("2020-10-11 12:40")
        stampDateArray.add("2020-10-11 12:40")
        stampDateArray.add("2020-10-11 12:40")
        stampDateArray.add("2020-10-11 12:40")
        stampDateArray.add("2020-10-11 12:40")
        stampDateArray.add("2020-10-11 12:40")
        stampDateArray.add("2020-10-11 12:40")
        stampDateArray.add("2020-10-11 12:40")
        stampDateArray.add("2020-10-11 12:40")
        stampDateArray.add("2020-10-11 12:40")
        stampDateArray.add("2020-10-11 12:40")



        recyclerView.setHasFixedSize(true);
        var adapter = StampViewAdapter(stampTitleArray, stampDateArray)
        recyclerView.layoutManager =  LinearLayoutManager(activity)
        recyclerView.adapter = adapter



        return rootView
    }

    companion object {

    }
}