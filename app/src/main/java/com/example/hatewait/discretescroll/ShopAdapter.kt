package com.example.hatewait.discretescroll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R


class ShopAdapter(private val data: List<Coupon>) : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_coupon_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Glide.with(holder.itemView.context)
//            .load(data[position].image)
//            .into(holder.image)
        holder.couponBenefitTextView.text = " ${data[position].benefit}"
        holder.couponRemarkTextView.text = " (${data[position].remark})"
        holder.couponIssueDateTextView.text = "발급날짜: ${data[position].issue_date}"
        holder.couponExpTextView.text = "유효기간: ${data[position].expiration_date}"
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        val image: ImageView = itemView.findViewById(R.id.image)
        val couponBenefitTextView: TextView = itemView.findViewById(R.id.coupon_benefit_text_view)
        val couponRemarkTextView: TextView = itemView.findViewById(R.id.coupon_remark_text_view)
        val couponIssueDateTextView: TextView = itemView.findViewById(R.id.coupon_issue_date_text_view)
        val couponExpTextView: TextView = itemView.findViewById(R.id.coupon_exp_text_view)
    }
}