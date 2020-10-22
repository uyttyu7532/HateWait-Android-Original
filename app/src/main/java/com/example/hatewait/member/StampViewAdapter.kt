package com.example.hatewait.member


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R
import kotlin.collections.ArrayList

class StampViewAdapter(

    private var stampTitleArray: ArrayList<String>,
    private var stampDateArray: ArrayList<String>

) : RecyclerView.Adapter<StampViewAdapter.MyViewHolder>() {


//    interface onItemClickListener {
//        fun onItemClick(holder: MyViewHolder, view: View, data: String, position: Int)
//    }
//
//    var itemClickListener: onItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.stamp_list_row, parent, false)

        return MyViewHolder(v)

    }

    // 아이템의 데이터 갯수
    override fun getItemCount(): Int {
        return stampTitleArray.size
    }

    // 뷰홀더에 해당하는 것이 전달됨.(내용만 교체할때 호출됨)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.stampTitleView.text = stampTitleArray[position]
        holder.stampDateView.text = stampDateArray[position]
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var stampTitleView = itemView.findViewById(R.id.stamp_title_view) as TextView
        var stampDateView = itemView.findViewById(R.id.stamp_date_view) as TextView
    }


}