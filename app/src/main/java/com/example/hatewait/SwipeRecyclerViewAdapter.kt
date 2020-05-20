package com.example.hatewait

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.backgroundColorResource
import java.util.*


class SwipeRecyclerViewAdapter(val items: ArrayList<ClientData>) :
    RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder>() {

    interface onItemClickListener {
        fun onItemClick(holder: SimpleViewHolder, view: View, position: Int)
    }

    var itemClickListener: onItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return SimpleViewHolder(view)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    //  ViewHolder Class
    inner class SimpleViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var swipeLayout = itemView.findViewById(R.id.swipeLayout) as SwipeLayout
        var clientView = itemView.findViewById(R.id.clientView) as CardView
        var clientNameView = itemView.findViewById(R.id.clientNameView) as TextView
        var clientNumView = itemView.findViewById(R.id.clientNumView) as TextView
        var clientPhoneView = itemView.findViewById(R.id.clientPhoneView) as TextView
        var detailView = itemView.findViewById(R.id.detailView) as CardView
        var detailView1 = itemView.findViewById(R.id.detailView1) as TextView
        var detailView2 = itemView.findViewById(R.id.detailView2) as TextView
        var delBtn = itemView.findViewById(R.id.delBtn) as ImageButton
        var bottom_wrapper_left = itemView.findViewById(R.id.bottom_wrapper_left) as FrameLayout
        var callBtn = itemView.findViewById(R.id.callBtn) as ImageButton


        init {

            // !!!!!!!callBtn과 swipeLayout clicklistener 매우이상!!!!!!!!!

            callBtn.setOnClickListener{
                bottom_wrapper_left.backgroundColorResource= R.color.colorCall
//                Toasty.warning(itemView, "호출되었습니다.", Toast.LENGTH_SHORT, true).show()
            }

            clientView.setOnClickListener {
                itemClickListener?.onItemClick(this, itemView, adapterPosition)
            }

        }
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


        // db목록에서 대기손님지우기?
        viewHolder.delBtn.setOnClickListener { view ->
            mItemManger.removeShownLayouts(viewHolder.swipeLayout)
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
            mItemManger.closeAllItems()
            Toasty.error(view.context, "삭제되었습니다", Toast.LENGTH_SHORT, true).show()
        }

//        viewHolder.setIsRecyclable(false)

//        viewHolder.clientView.setOnClickListener { view->
////            itemClickListener?.onItemClick(viewHolder, view, position)
//            Toasty.error(view.context, position.toString(), Toast.LENGTH_SHORT, true).show()
//            if (viewHolder.detailView.visibility == View.GONE) {
//                viewHolder.detailView.visibility = View.VISIBLE
//            } else {
//                viewHolder.detailView.visibility = View.GONE
//            }


//        }


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position)
    }


    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipeLayout
    }


}