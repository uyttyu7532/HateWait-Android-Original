package com.example.hatewait.member

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R
import com.example.hatewait.login.memberInfo
import com.example.hatewait.model.StoreListInfo
import java.util.*
import kotlin.collections.ArrayList

class StoreListAdapter(

    private var storeListArray: ArrayList<StoreListInfo>

) : RecyclerView.Adapter<StoreListAdapter.MyViewHolder>(), Filterable {


    interface onItemClickListener {
        fun onItemClick(holder: MyViewHolder, view: View, data: String, position: Int)
    }

    var itemsFilterList = ArrayList<StoreListInfo>()

    var itemClickListener: onItemClickListener? = null

    init {
        itemsFilterList = storeListArray!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.store_list_row, parent, false)

        return MyViewHolder(v)

    }

    // 아이템의 데이터 갯수
    override fun getItemCount(): Int {
        return itemsFilterList.size
    }

    // 뷰홀더에 해당하는 것이 전달됨.(내용만 교체할때 호출됨)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.storeNameTextView.text = itemsFilterList[position].store_name
        holder.storeStampCouponTextView.text = "스탬프 ${itemsFilterList[position].stamp_count}/${itemsFilterList[position].maximum_stamp}  쿠폰 ${itemsFilterList[position].coupon_count}"

        holder.storeListCardView.setOnClickListener{
            val intent = Intent(StoreListContext, ManageStampCouponActivity::class.java)
            intent.putExtra("memberId", memberInfo!!.id)
            intent.putExtra("storeId", itemsFilterList[position].store_id)
            intent.putExtra("store_name", itemsFilterList[position].store_name)
            intent.putExtra("stamp_benefit", itemsFilterList[position].benefit_description)
            intent.putExtra("stamp_count", itemsFilterList[position].stamp_count)
            intent.putExtra("maximum_stamp", itemsFilterList[position].maximum_stamp)
            StoreListContext.startActivity(intent)

        }
        holder.storeBenefitTextView.text = itemsFilterList[position].benefit_description

    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var storeNameTextView = itemView.findViewById(R.id.store_name_text_view) as TextView
        var storeStampCouponTextView = itemView.findViewById(R.id.store_stamp_coupon_text_view) as TextView
        var storeListCardView = itemView.findViewById(R.id.store_list_card_view) as CardView
        var storeBenefitTextView = itemView.findViewById(R.id.store_benefit_text_view) as TextView


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    itemsFilterList = storeListArray
                } else {
                    val resultList = ArrayList<StoreListInfo>()
                    for (row in storeListArray!!) {
                        if (row.store_name.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    itemsFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = itemsFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemsFilterList = results?.values as ArrayList<StoreListInfo>
                notifyDataSetChanged()
            }

        }
    }


}