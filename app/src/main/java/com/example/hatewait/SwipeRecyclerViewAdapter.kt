package com.example.hatewait

import android.content.SharedPreferences
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
import kotlinx.android.synthetic.main.row.view.*
import org.jetbrains.anko.backgroundColorResource
import java.util.*
import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.hatewait.fcm.FcmPush
import com.example.hatewait.socket.*
import kotlin.collections.ArrayList


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
            LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return SimpleViewHolder(v)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    inner class SimpleViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val swipeLayout = itemView.findViewById(R.id.swipeLayout) as SwipeLayout
        val clientView = itemView.findViewById(R.id.clientView) as CardView
        val detailView = itemView.findViewById(R.id.detailView) as CardView
        val clientNameView = itemView.findViewById(R.id.clientNameView) as TextView
        val clientNumView = itemView.findViewById(R.id.clientNumView) as TextView
        val clientPhoneView = itemView.findViewById(R.id.clientPhoneView) as TextView
        val detailView1 = itemView.findViewById(R.id.detailView1) as TextView
        val detailView2 = itemView.findViewById(R.id.detailView2) as TextView
        val delBtn = itemView.findViewById(R.id.delBtn) as ImageButton
        val bottom_wrapper_left = itemView.findViewById(R.id.bottom_wrapper_left) as FrameLayout
        val callBtn = itemView.callBtn


        init {
            callBtn.setOnClickListener { v ->
                val position = adapterPosition
                if (called.containsKey(items[position].phone) && called[items[position].phone]!!) {
                    //TODO 원래는 이 if의 내용이 없어야 함!!!
                    setShared(pref, items[position].phone, false)
                    called[items[position].phone] = false
                    this.bottom_wrapper_left.backgroundColorResource = R.color.white
                } else {
                    setShared(pref, items[position].phone, true)
                    called[items[position].phone] = true
                    this.bottom_wrapper_left.backgroundColorResource = R.color.colorCall
                    Toasty.warning(
                        itemView.context,
                        items[position].name + " 손님 호출 완료",
                        Toast.LENGTH_SHORT,
                        true
                    ).show()


                    callCustomer(
                        items[position].phone,
                        items[position].id,
                        "[${STORENAME}] ${AUTONUM}번째 순서 전 입니다. 가게 앞으로 와주세요."
                    )

                }
            }

            clientView.setOnClickListener { v ->
                val position = adapterPosition
                if (clicked.containsKey(items[position].phone) && clicked[items[position].phone]!!) {
                    clicked[items[position].phone] = false
                    detailView.visibility = View.GONE

                } else {

                    clicked[items[position].phone] = true

                    detailView.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun onBindViewHolder(
        viewHolder: SimpleViewHolder,
        position: Int
    ) {

        val item: ClientData = items[position]


        viewHolder.clientNameView.text = item.name
        viewHolder.clientNumView.text = "(" + item.peopleNum + "명)"
        viewHolder.clientPhoneView.text = "0" + item.phone

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
        viewHolder.delBtn.setOnClickListener { view ->
            SweetAlertDialog(view.context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("${items[position].name} 손님을 정말로 리스트에서 삭제하시겠습니까?")
                .setContentText("\n")
                .setConfirmText("삭제")
                .setConfirmClickListener { sDialog ->

                    Toasty.error(
                        view.context,
                        "${items[position].name} 손님 삭제 완료",
                        Toast.LENGTH_SHORT,
                        true
                    ).show()

                    DelCustomerAsyncTask().execute(items[position].id)


                    mItemManger.removeShownLayouts(viewHolder.swipeLayout)
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                    mItemManger.closeAllItems()

                    // 호출한 손님 목록에서도 지우기
                    if (called.containsKey(items[position].phone) && called[items[position].phone]!!) {
                        setShared(pref, items[position].phone, false)
                        called[items[position].phone] = false
                    }

                    StoreWaitingListAsyncTask().execute()
                    sDialog.dismissWithAnimation()
                }
                .setCancelButton(
                    "아니오"
                ) { sDialog -> sDialog.dismissWithAnimation() }
                .show()


        }

        if (called.containsKey(items[position].phone) && called[items[position].phone]!!) {
            viewHolder.bottom_wrapper_left.backgroundColorResource = R.color.colorCall
        } else {
            viewHolder.bottom_wrapper_left.backgroundColorResource = R.color.white
        }

        if (clicked.containsKey(items[position].phone) && clicked[items[position].phone]!!) {
            viewHolder.detailView.visibility = View.VISIBLE
        } else {
            viewHolder.detailView.visibility = View.GONE
        }


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position)
    }


    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipeLayout
    }
}


fun callCustomer(phone: String, id: String, message: String) {
    //푸시를 받을 유저의 UID가 담긴 destinationUid 값을 넣어준후 fcmPush클래스의 sendMessage 메소드 호출
    val fcmPush = FcmPush()
    fcmPush?.sendMessage(phone, message)
    // 서버쪽에서 문자메시지 보내기
    PushMessageAsyncTask().execute(id)
}
