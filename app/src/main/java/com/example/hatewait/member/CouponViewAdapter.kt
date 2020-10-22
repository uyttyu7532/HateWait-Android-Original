package com.example.hatewait.member


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R
import kotlin.collections.ArrayList

class CouponViewAdapter(

    private var couponTitleArray: ArrayList<String>,
    private var couponDateArray: ArrayList<String>

) : RecyclerView.Adapter<CouponViewAdapter.MyViewHolder>() {


//    interface onItemClickListener {
//        fun onItemClick(holder: MyViewHolder, view: View, data: String, position: Int)
//    }
//
//    var itemClickListener: onItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.coupon_list_row, parent, false)

        return MyViewHolder(v)

    }

    // 아이템의 데이터 갯수
    override fun getItemCount(): Int {
        return couponTitleArray.size
    }

    // 뷰홀더에 해당하는 것이 전달됨.(내용만 교체할때 호출됨)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.couponTitleView.text = couponTitleArray[position]
        holder.couponDateView.text = couponDateArray[position]
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var couponTitleView = itemView.findViewById(R.id.coupon_title_view) as TextView
        var couponDateView = itemView.findViewById(R.id.coupon_date_view) as TextView
    }

}