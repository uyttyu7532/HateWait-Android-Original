import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R
import com.example.hatewait.model.CouponMember
import java.util.*
import kotlin.collections.ArrayList

class CouponMemberViewAdapter(

    private var couponMemberListArray: ArrayList<CouponMember>

) : RecyclerView.Adapter<CouponMemberViewAdapter.MyViewHolder>(), Filterable {


    interface onItemClickListener {
        fun onItemClick(holder: MyViewHolder, view: View, data: String, position: Int)
    }

    var itemsFilterList = ArrayList<CouponMember>()

    var itemClickListener: onItemClickListener? = null

    init {
        itemsFilterList = couponMemberListArray!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.coupon_member_list_row, parent, false)

        return MyViewHolder(v)

    }

    // 아이템의 데이터 갯수
    override fun getItemCount(): Int {
        return itemsFilterList.size
    }

    // 뷰홀더에 해당하는 것이 전달됨.(내용만 교체할때 호출됨)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.visitorNameView.text = itemsFilterList[position].member_name
        holder.memberPhoneTextView.text = itemsFilterList[position].member_phone
        holder.couponCount.text = itemsFilterList[position].coupon_count.toString()

        holder.useCouponBottomWrapperLeft
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var visitorNameView = itemView.findViewById(R.id.visitor_name_view) as TextView
        var memberPhoneTextView = itemView.findViewById(R.id.member_phone_text_view) as TextView
        var couponCount = itemView.findViewById(R.id.coupon_count) as TextView
        var useCouponBottomWrapperLeft =
            itemView.findViewById(R.id.use_coupon_bottom_wrapper_left) as FrameLayout

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    itemsFilterList = couponMemberListArray
                } else {
                    val resultList = ArrayList<CouponMember>()
                    for (row in couponMemberListArray!!) {
                        if(row.member_name.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ){
                            resultList.add(row)
                        }
                        else if(row.member_phone.toLowerCase(Locale.ROOT)
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
                itemsFilterList = results?.values as ArrayList<CouponMember>
                notifyDataSetChanged()
            }

        }
    }


}