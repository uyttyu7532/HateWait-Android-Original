package com.example.hatewait.discretescroll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hatewait.R


class ShopAdapter(private val data: List<Item>) : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Glide.with(holder.itemView.context)
//            .load(data[position].image)
//            .into(holder.image)
        holder.test.text = " ${data[position].id} ${data[position].name} ${data[position].price}"
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val image: ImageView = itemView.findViewById(R.id.image)
        val test: TextView = itemView.findViewById(R.id.test_text_view)
    }
}