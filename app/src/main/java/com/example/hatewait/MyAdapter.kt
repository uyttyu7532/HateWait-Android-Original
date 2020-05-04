package com.example.hatewait

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// RecyclerView에 표시될 View 생성
class MyAdapter(val items: ArrayList<ClientData>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    interface onItemClickListener{
       fun onItemClick( holder:MyViewHolder, view: View, data:String, position: Int)
    }

    var itemClickListener:onItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // row에 해당하는 객체에 inflation되서 View로 전달. View에서는 textView에 해당하는 것을 참고
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row,parent, false)
        return MyViewHolder(v)

    }
    // 아이템의 데이터 갯수
    override fun getItemCount(): Int {
        return items.size
    }

    // 뷰홀더에 해당하는 것이 전달됨.(내용만 교체할때 호출됨)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.clientView.text=items[position].name+" "+items[position].people_num+"명 "+items[position].phone
        holder.detailView.text=items[position].id +" "+items[position].is_member
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var clientView:TextView = itemView.findViewById(R.id.clientView)
        var detailView:TextView = itemView.findViewById(R.id.detailView)
//        var delBtn: ImageButton = itemView.findViewById(R.id.delBtn)

        init{
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(this, it, items[adapterPosition].name,adapterPosition)
            }

//            delBtn.setOnClickListener {
//                removeItem(adapterPosition)
//            }
        }
    }

    fun moveItem(oldPos:Int, newPos:Int){
        val item = items.get(oldPos)
        items.removeAt(oldPos)
        items.add(newPos,item)
        notifyItemMoved(oldPos,newPos)
    }

    fun removeItem(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }





}