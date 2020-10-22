package com.example.hatewait.member

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R
import java.util.*
import kotlin.collections.ArrayList

class StoreListAdapter(

    private var storeListArray: ArrayList<String>

) : RecyclerView.Adapter<StoreListAdapter.MyViewHolder>(), Filterable {


    interface onItemClickListener {
        fun onItemClick(holder: MyViewHolder, view: View, data: String, position: Int)
    }

    var itemsFilterList = ArrayList<String>()

    var itemClickListener: onItemClickListener? = null

    init {
        itemsFilterList = storeListArray
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
        holder.storeNameTextView.text = itemsFilterList[position]
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var storeNameTextView = itemView.findViewById(R.id.store_name_text_view) as TextView
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    itemsFilterList = storeListArray
                } else {
                    val resultList = ArrayList<String>()
                    for (row in storeListArray) {
                        if (row.toLowerCase(Locale.ROOT)
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
                itemsFilterList = results?.values as ArrayList<String>
                notifyDataSetChanged()
            }

        }
    }


}