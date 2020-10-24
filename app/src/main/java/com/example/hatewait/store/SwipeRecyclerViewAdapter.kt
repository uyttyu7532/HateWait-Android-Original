package com.example.hatewait.store

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.example.hatewait.R
import com.example.hatewait.fcm.FcmPush
import com.example.hatewait.model.ClientData
import com.example.hatewait.model.setShared
import com.example.hatewait.socket.*
import kotlinx.android.synthetic.main.waiting_list_row.view.*
import org.jetbrains.anko.backgroundColorResource
import java.util.*


class SwipeRecyclerViewAdapter(
    val items: ArrayList<ClientData>,
    val called: HashMap<String, Boolean>,
    val clicked: HashMap<String, Boolean>,
    val pref: SharedPreferences,
    val context: Context
) :
    RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder>() {


    interface onItemClickListener {
        fun onItemClick(holder: SimpleViewHolder, view: View, position: Int)
    }

    var itemClickListener: onItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.waiting_list_row, parent, false)
        return SimpleViewHolder(v)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    inner class SimpleViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val waitingListSwipeLayout = itemView.findViewById(R.id.waiting_list_swipe_layout) as SwipeLayout
        val waitingListCardView= itemView.findViewById(R.id.waiting_list_card_View) as CardView
        val waitingListDetailView = itemView.findViewById(R.id.waiting_list_detail_view) as CardView
        val waitingNameTextView = itemView.findViewById(R.id.waiting_name_text_view) as TextView
        val waitingNumTextView = itemView.findViewById(R.id.waiting_num_text_view) as TextView
        val waitingPhoneTextView = itemView.findViewById(R.id.waiting_phone_text_view) as TextView
        val waitingListDetailTextView = itemView.findViewById(R.id.waiting_list_detail_text_view) as TextView
        val waitingListDetailTextView2 = itemView.findViewById(R.id.waiting_list_detail_text_view2) as TextView
        val waitingListDeleteButton = itemView.findViewById(R.id.waiting_list_delete_button) as ImageButton
        val bottomWrapperLeft = itemView.findViewById(R.id.bottom_wrapper_left) as FrameLayout
        val waitingListCallButton: ImageButton = itemView.waiting_list_call_button


        init {
            waitingListCallButton.setOnClickListener {
                val position = adapterPosition
                if (called.containsKey(items[position].phone) && called[items[position].phone]!!) {
                    //TODO 원래는 이 if의 내용이 없어야 함!!!
                    setShared(
                        pref,
                        items[position].phone,
                        false
                    )
                    called[items[position].phone] = false
                    this.bottomWrapperLeft.backgroundColorResource =
                        R.color.white
                } else {
                    setShared(
                        pref,
                        items[position].phone,
                        true
                    )
                    called[items[position].phone] = true
                    this.bottomWrapperLeft.backgroundColorResource =
                        R.color.colorCall
//                    Toast.Config.getInstance().allowQueue(true).apply()
                    Toast.makeText(
                        itemView.context,
                        items[position].name + " 손님 호출 완료",
                        Toast.LENGTH_SHORT
                    ).show()


                    callCustomer(
                        items[position].id,
                        "[${STORENAME}] ${items[position].turn}번째 순서 전 입니다. 가게 앞으로 와주세요."
                    )
                }
            }

            waitingListCardView.setOnClickListener {
                val position = adapterPosition

//                if (clicked.containsKey(items[position].phone) && clicked[items[position].phone]!!) {
//                    clicked[items[position].phone] = false
//                    detailView.visibility = View.GONE
//                } else {
//                    clicked[items[position].phone] = true
//                    detailView.visibility = View.VISIBLE
//                }

                val callIntent =
                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:0" + items[position].phone))
                callIntent.flags = FLAG_ACTIVITY_NEW_TASK
                context.startActivity(callIntent)
            }
        }
    }


    override fun onBindViewHolder(
        viewHolder: SimpleViewHolder,
        position: Int
    ) {

        val item: ClientData = items[position]


        viewHolder.waitingNameTextView.text = item.name
        viewHolder.waitingNumTextView.text = "(" + item.peopleNum + "명)"
        viewHolder.waitingPhoneTextView.text = "0" + item.phone

//        viewHolder.detailView1.text =
//            "대기열에 추가된 시간: 2020-00-00-00:00:00"
//        viewHolder.detailView2.text =
//            "최근에 알림 보낸시간: 2020-00-00-00:00:00"

        viewHolder.waitingListSwipeLayout.showMode = SwipeLayout.ShowMode.PullOut
        // Drag From Left
//        viewHolder.swipeLayout.addDrag(
//            SwipeLayout.DragEdge.Left,
//            viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper_left)
//        )
        // Drag From Right
        viewHolder.waitingListSwipeLayout.addDrag(
            SwipeLayout.DragEdge.Right,
            viewHolder.waitingListSwipeLayout.findViewById(R.id.bottom_wrapper_right)
        )
        // Handling different events when swiping
        viewHolder.waitingListSwipeLayout.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onClose(layout: SwipeLayout) { //when the SurfaceView totally cover the BottomView.
            }

            override fun onUpdate(
                layout: SwipeLayout,
                leftOffset: Int,
                topOffset: Int
            ) {
            }

            override fun onStartOpen(layout: SwipeLayout) {
            }

            override fun onOpen(layout: SwipeLayout) { //when the BottomView totally show.
            }

            override fun onStartClose(layout: SwipeLayout) {}
            override fun onHandRelease(
                layout: SwipeLayout,
                xvel: Float,
                yvel: Float
            ) {
            }
        })


        // db목록에서 대기손님지우기?
        viewHolder.waitingListDeleteButton.setOnClickListener { view ->
            SweetAlertDialog(view.context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("${items[position].name} 손님을 정말로 리스트에서 삭제하시겠습니까?")
                .setContentText("\n")
                .setConfirmText("삭제")
                .setConfirmClickListener { sDialog ->

                    // 호출한 손님 목록에서도 지우기 (제대로 동작하나 모르겠다.)
                    if (called.containsKey(items[position].phone) && called[items[position].phone]!!) {
                        setShared(
                            pref,
                            items[position].phone,
                            false
                        )
                        called[items[position].phone] = false
                    }

                    Toast.makeText(
                        view.context,
                        "${items[position].name} 손님 삭제 완료",
                        Toast.LENGTH_SHORT
                    ).show()

                    DelCustomerAsyncTask().execute(items[position].id)


                    mItemManger.removeShownLayouts(viewHolder.waitingListSwipeLayout)
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                    mItemManger.closeAllItems()


                    StoreWaitingListAsyncTask().execute()

                    sDialog.dismissWithAnimation()
                }
                .setCancelButton(
                    "아니오"
                ) { sDialog -> sDialog.dismissWithAnimation() }
                .show()
        }


        if (called.containsKey(items[position].phone) && called[items[position].phone]!!) {
            viewHolder.bottomWrapperLeft.backgroundColorResource =
                R.color.colorCall
        } else {
            viewHolder.bottomWrapperLeft.backgroundColorResource =
                R.color.white
        }

        if (clicked.containsKey(items[position].phone) && clicked[items[position].phone]!!) {
            viewHolder.waitingListDetailTextView.visibility = View.VISIBLE
        } else {
            viewHolder.waitingListDetailTextView.visibility = View.GONE
        }


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position)
    }


    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.waiting_list_swipe_layout
    }
}


fun callCustomer(customerId: String, message: String) {
    //푸시를 받을 유저의 UID가 담긴 destinationUid 값을 넣어준후 fcmPush클래스의 sendMessage 메소드 호출
    val fcmPush = FcmPush()
    fcmPush?.sendMessage(customerId, message)
    // 서버쪽에서 문자메시지 보내기
    PushMessageAsyncTask().execute(customerId)
    Log.i("로그", "callCustomer 호출하는손님id:${customerId}")
}
