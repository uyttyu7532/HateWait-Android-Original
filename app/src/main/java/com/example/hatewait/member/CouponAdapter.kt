package com.example.hatewait.member

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R
import com.example.hatewait.model.CouponListInfo
import java.text.SimpleDateFormat
import java.util.*


class CouponAdapter(private val data: List<CouponListInfo>) : RecyclerView.Adapter<CouponAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_coupon_card, parent, false)
        return ViewHolder(v)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Glide.with(holder.itemView.context)
//            .load(data[position].image)
//            .into(holder.image)
//        holder.couponBenefitTextView.text = " ${data[position].}" 혜택
//        holder.couponRemarkTextView.text = " (${data[position].remark})" 설명




//        val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss", Locale.KOREA)
//        holder.couponIssueDateTextView.text = "발급날짜: ${SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(data[position].issue_date))}"
//        holder.couponExpTextView.text = "유효기간: ${SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(data[position].expiration_date))}"


        holder.couponIssueDateTextView.text = "발급날짜: ${SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(data[position].issue_date)}"
        holder.couponExpTextView.text = "유효기간: ${SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(data[position].expiration_date)}"

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