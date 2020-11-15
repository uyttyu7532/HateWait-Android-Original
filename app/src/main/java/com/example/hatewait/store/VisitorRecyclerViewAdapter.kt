
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R
import java.util.*
import kotlin.collections.ArrayList

// RecyclerView에 표시될 View 생성
class VisitorRecyclerViewAdapter(

    val items: ArrayList<String>,
    var switchOn: Boolean

) : RecyclerView.Adapter<VisitorRecyclerViewAdapter.MyViewHolder>(), Filterable {


    interface onItemClickListener {
        fun onItemClick(holder: MyViewHolder, view: View, data: String, position: Int)
    }

    var itemsFilterList = ArrayList<String>()
    var itemClickListener: onItemClickListener? = null
//    private val mSelectedItems = SparseBooleanArray(0)


    init {
        itemsFilterList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // row에 해당하는 객체에 inflation되서 View로 전달. View에서는 textView에 해당하는 것을 참고
        val v = LayoutInflater.from(parent.context).inflate(R.layout.visitor_list_row, parent, false)

        return MyViewHolder(v)

    }

    // 아이템의 데이터 갯수
    override fun getItemCount(): Int {
        return itemsFilterList.size
    }

    // 뷰홀더에 해당하는 것이 전달됨.(내용만 교체할때 호출됨)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.visitorNameView.text = itemsFilterList[position]
//        holder.meaningView.text = words.getValue(itemsFilterList[position])

//        if (mSelectedItems.get(position, false)) {
//            holder.visitorListDetailView.visibility =VISIBLE
//        } else {
//            holder.visitorListDetailView.visibility = GONE
//        }

    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val visitorNameView: TextView = itemView.findViewById(R.id.visitor_name_view)
//        val visitorListDetailView: CardView = itemView.findViewById(R.id.visitor_list_detail_view)



        init {

//            // 전체보이기/숨기기
//            if (switchOn) {
//                meaningView.visibility = VISIBLE
//            } else {
//                meaningView.visibility = GONE
//            }

            itemView.setOnClickListener {
//                if (mSelectedItems.get(adapterPosition, false)) {
//                    mSelectedItems.put(adapterPosition, false)
//                }
//                else{
//                    mSelectedItems.put(adapterPosition, true)
//                }
                itemClickListener?.onItemClick(this, it, items[adapterPosition], adapterPosition)
            }



        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    itemsFilterList = items
                } else {
                    val resultList = ArrayList<String>()
                    for (row in items) {
                        if (row.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
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

    fun moveItem(oldPos: Int, newPos: Int) {
        val item = items.get(oldPos)
        items.removeAt(oldPos)
        items.add(newPos, item)
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos: Int): String {
        notifyItemRemoved(pos)
        return items.removeAt(pos)
    }

}