package com.example.hatewait

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import kotlinx.android.synthetic.main.row.view.*
import java.util.*


class SwipeRecyclerViewAdapter(val items: ArrayList<ClientData>) :
    RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return SimpleViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        viewHolder: SimpleViewHolder,
        position: Int
    ) {

        val item: ClientData = items[position]


        viewHolder.clientNameView.text = item.name
        viewHolder.clientNumView.text = "(" + item.people_num + "명)"
        viewHolder.clientPhoneView.text = item.phone

        viewHolder.detailView1.text =
            "대기열에 추가된 시간: 2020-05-03-09:14:02"
        viewHolder.detailView2.text =
            "최근에 알림 보낸시간: 2020-05-08-09:40:15"

        viewHolder.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
        // Drag From Left
//        viewHolder.swipeLayout.addDrag(
//            SwipeLayout.DragEdge.Left,
//            viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper_left)
//        )
        // Drag From Right
        viewHolder.swipeLayout.addDrag(
            SwipeLayout.DragEdge.Right,
            viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper_right)
        )
        // Handling different events when swiping
        viewHolder.swipeLayout.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onClose(layout: SwipeLayout) { //when the SurfaceView totally cover the BottomView.
            }

            override fun onUpdate(
                layout: SwipeLayout,
                leftOffset: Int,
                topOffset: Int
            ) { //you are swiping.
            }

            override fun onStartOpen(layout: SwipeLayout) {

            }

            override fun onOpen(layout: SwipeLayout) { //when the BottomView totally show.

//                if(layout.isRightSwipeEnabled){
//                    mode = Attributes.Mode.Single
//                }
//
//                if(layout.isLeftSwipeEnabled){
//                    mode = Attributes.Mode.Multiple
//                }
            }

            override fun onStartClose(layout: SwipeLayout) {}
            override fun onHandRelease(
                layout: SwipeLayout,
                xvel: Float,
                yvel: Float
            ) { //when user's hand released.
            }
        })

        viewHolder.swipeLayout.clientView
            .setOnClickListener {
                if (viewHolder.detailView.visibility == View.GONE) {
                    viewHolder.detailView.visibility = View.VISIBLE
                } else {
                    viewHolder.detailView.visibility = View.GONE
                }
            }

        // db목록에서 대기손님지우기?
        viewHolder.delBtn.setOnClickListener { view ->
            mItemManger.removeShownLayouts(viewHolder.swipeLayout)
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
            mItemManger.closeAllItems()
        }


        //호출하면 배경색 변경
//        viewHolder.callBtn.setOnClickListener { view ->
//            view.bottom_wrapper_left.backgroundColor = Color.GRAY
//        }

        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipeLayout
    }

    //  ViewHolder Class
    class SimpleViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var swipeLayout: SwipeLayout
        var clientView: CardView
        var clientNameView: TextView
        var clientNumView: TextView
        var clientPhoneView: TextView
        var detailView: CardView
        var detailView1: TextView
        var detailView2: TextView
        var delBtn: ImageButton
        var bottom_wrapper_left: FrameLayout
        var callBtn: ImageButton

        init {
            swipeLayout = itemView.findViewById(R.id.swipeLayout) as SwipeLayout
            clientView = itemView.findViewById(R.id.clientView) as CardView
            clientNameView = itemView.findViewById(R.id.clientNameView) as TextView
            clientNumView = itemView.findViewById(R.id.clientNumView) as TextView
            clientPhoneView = itemView.findViewById(R.id.clientPhoneView) as TextView
            detailView = itemView.findViewById(R.id.detailView) as CardView
            detailView1 = itemView.findViewById(R.id.detailView1) as TextView
            detailView2 = itemView.findViewById(R.id.detailView2) as TextView
            delBtn = itemView.findViewById(R.id.delBtn) as ImageButton
            bottom_wrapper_left = itemView.findViewById(R.id.bottom_wrapper_right) as FrameLayout
            callBtn = itemView.findViewById(R.id.callBtn) as ImageButton
        }
    }

}