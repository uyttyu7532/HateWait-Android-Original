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
import com.example.hatewait.login.storeInfo
import com.example.hatewait.model.CallWaitingResponseData
import com.example.hatewait.model.ClientData
import com.example.hatewait.model.DeleteWaitingResponseData
import com.example.hatewait.model.WaitingInfo
import com.example.hatewait.retrofit2.MyApi
import com.example.hatewait.socket.*
import kotlinx.android.synthetic.main.waiting_list_row.view.*
import org.jetbrains.anko.backgroundColorResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SwipeRecyclerViewAdapter(
    val items: List<WaitingInfo>,
//    val called: HashMap<String, Boolean>,
    val clicked: HashMap<String, Boolean>,
//    val pref: SharedPreferences,
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
        val waitingListSwipeLayout =
            itemView.findViewById(R.id.waiting_list_swipe_layout) as SwipeLayout
        val waitingListCardView = itemView.findViewById(R.id.waiting_list_card_View) as CardView
        val waitingListDetailView = itemView.findViewById(R.id.waiting_list_detail_view) as CardView
        val waitingNameTextView = itemView.findViewById(R.id.waiting_name_text_view) as TextView
        val waitingNumTextView = itemView.findViewById(R.id.waiting_num_text_view) as TextView
        val waitingPhoneTextView = itemView.findViewById(R.id.waiting_phone_text_view) as TextView
        val waitingListDetailTextView =
            itemView.findViewById(R.id.waiting_list_detail_text_view) as TextView

        //        val waitingListDetailTextView2 =
//            itemView.findViewById(R.id.waiting_list_detail_text_view2) as TextView
        val waitingListDeleteButton =
            itemView.findViewById(R.id.waiting_list_delete_button) as ImageButton
        val bottomWrapperLeft = itemView.findViewById(R.id.bottom_wrapper_left) as FrameLayout
        val waitingListCallButton: ImageButton = itemView.waiting_list_call_button


        init {
            waitingListCallButton.setOnClickListener {
                val position = adapterPosition

                if (items[position].called_time.equals("0000-00-00 00:00:00") || items[position].called_time == null) {


                    MyApi.WaitingListService.requestWaitingCall(
                        id = storeInfo!!.id,
                        phone = items[position].phone
                    )
                        .enqueue(object : Callback<CallWaitingResponseData> {
                            override fun onFailure(call: Call<CallWaitingResponseData>, t: Throwable) {
                                Log.d("retrofit2 대기손님호출 :: ", "연결실패 $t")
                            }

                            override fun onResponse(
                                call: Call<CallWaitingResponseData>,
                                response: Response<CallWaitingResponseData>
                            ) {
                                var data: CallWaitingResponseData? = response?.body()
                                Log.d("retrofit2 대기손님호출 ::", response.code().toString() + response.body().toString())
                                when (response.code()) {
                                    200 -> {
                                        //                    callCustomer(
//                        items[position].id,
//                        "[${STORENAME}] ${items[position].turn}번째 순서 전 입니다. 가게 앞으로 와주세요."
//                    )
                                        getWaitingList()
                                    }
                                }
                            }
                        }
                        )


                    // TODO 호출성공하면 밑에 실행
                    this.bottomWrapperLeft.backgroundColorResource =
                        R.color.colorCall
                    Toast.makeText(
                        itemView.context,
                        items[position].name + " 손님 호출 완료",
                        Toast.LENGTH_SHORT
                    ).show()
                } else { // 호출 o

                }


//                if (called.containsKey(items[position].phone) && called[items[position].phone]!!) {
//                    setShared(
//                        pref,
//                        items[position].phone,
//                        false
//                    )
//                    called[items[position].phone] = false
//                    this.bottomWrapperLeft.backgroundColorResource =
//                        R.color.white
//                } else {
//                    setShared(
//                        pref,
//                        items[position].phone,
//                        true
//                    )
//                    called[items[position].phone] = true
//                    this.bottomWrapperLeft.backgroundColorResource =
//                        R.color.colorCall
////                    Toast.Config.getInstance().allowQueue(true).apply()
//                    Toast.makeText(
//                        itemView.context,
//                        items[position].name + " 손님 호출 완료",
//                        Toast.LENGTH_SHORT
//                    ).show()


//                    callCustomer(
//                        items[position].id,
//                        "[${STORENAME}] ${items[position].turn}번째 순서 전 입니다. 가게 앞으로 와주세요."
//                    )
//                }
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

//                val callIntent =
//                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:0" + items[position].phone))
//                callIntent.flags = FLAG_ACTIVITY_NEW_TASK
//                context.startActivity(callIntent)
            }
        }
    }


    override fun onBindViewHolder(
        viewHolder: SimpleViewHolder,
        position: Int
    ) {

        val item: WaitingInfo = items[position]


        viewHolder.waitingNameTextView.text = item.name
        viewHolder.waitingNumTextView.text = "(" + item.people_number + "명)"
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
            Log.d("retrofit2 ::", storeInfo.toString())
            SweetAlertDialog(view.context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("${items[position].name} 손님이 가게에 방문하셨나요??")
                .setContentText("\n")
                .setConfirmText("방문함")
                .setConfirmClickListener { sDialog ->

                    // 호출한 손님 목록에서도 지우기 (제대로 동작하나 모르겠다.)
//                    if (called.containsKey(items[position].phone) && called[items[position].phone]!!) {
//                        setShared(
//                            pref,
//                            items[position].phone,
//                            false
//                        )
//                        called[items[position].phone] = false
//                    }

                    Toast.makeText(
                        view.context,
                        "${items[position].name} 손님 삭제 완료",
                        Toast.LENGTH_SHORT
                    ).show()

//                    DelCustomerAsyncTask().execute(items[position].id)
//                    visited = true

                    MyApi.WaitingListService.requestDeleteWaiting(
                        userId = storeInfo!!.id,
                        deleteWaiting = DeleteWaitingResponseData(items[position].phone, true)
                    )
                        .enqueue(object : Callback<MyApi.onlyMessageResponseData> {
                            override fun onFailure(
                                call: Call<MyApi.onlyMessageResponseData>,
                                t: Throwable
                            ) {
                                Log.d("retrofit2 대기 삭제 :: ", "대기삭제 연결실패 $t")
                            }

                            override fun onResponse(
                                call: Call<MyApi.onlyMessageResponseData>,
                                response: Response<MyApi.onlyMessageResponseData>
                            ) {
                                var data: MyApi.onlyMessageResponseData? = response?.body()
                                Log.d(
                                    "retrofit2 대기 삭제 ::",
                                    response.code().toString() + response.body().toString()
                                )
                                when (response.code()) {
                                    409 -> {
                                    }
                                    200 -> {
                                        mItemManger.removeShownLayouts(viewHolder.waitingListSwipeLayout)
                                        notifyItemRemoved(position)
                                        notifyItemRangeChanged(position, items.size)
                                        mItemManger.closeAllItems()

                                        getWaitingList()
                                    }
                                }
                            }
                        }

                        )


//                    StoreWaitingListAsyncTask().execute()

                    sDialog.dismissWithAnimation()
                }
                .setCancelButton(
                    "방문안함"
                ) { sDialog ->
//                    visited = false
                    MyApi.WaitingListService.requestDeleteWaiting(
                        userId = storeInfo!!.id,
                        deleteWaiting = DeleteWaitingResponseData(items[position].phone, false)
                    )
                        .enqueue(object : Callback<MyApi.onlyMessageResponseData> {
                            override fun onFailure(
                                call: Call<MyApi.onlyMessageResponseData>,
                                t: Throwable
                            ) {
                                Log.d("retrofit2 대기 삭제 :: ", "대기삭제 연결실패 $t")
                            }

                            override fun onResponse(
                                call: Call<MyApi.onlyMessageResponseData>,
                                response: Response<MyApi.onlyMessageResponseData>
                            ) {
                                var data: MyApi.onlyMessageResponseData? = response?.body()
                                Log.d(
                                    "retrofit2 대기 삭제 ::",
                                    response.code().toString() + response.body().toString()
                                )
                                when (response.code()) {
                                    409 -> {
                                    }
                                    200 -> {
                                        mItemManger.removeShownLayouts(viewHolder.waitingListSwipeLayout)
                                        notifyItemRemoved(position)
                                        notifyItemRangeChanged(position, items.size)
                                        mItemManger.closeAllItems()

                                        getWaitingList()
                                    }
                                }
                            }
                        }
                        )
                    sDialog.dismissWithAnimation()
                }
                .show()


        }


//        if (called.containsKey(items[position].phone) && called[items[position].phone]!!) {
//            viewHolder.bottomWrapperLeft.backgroundColorResource =
//                R.color.colorCall
//        } else {
//            viewHolder.bottomWrapperLeft.backgroundColorResource =
//                R.color.white
//        }
        if (items[position].called_time.equals("0000-00-00 00:00:00") || items[position].called_time == null) { // 호출x
            viewHolder.bottomWrapperLeft.backgroundColorResource =
                R.color.white
        } else { // 호출o
            viewHolder.bottomWrapperLeft.backgroundColorResource =
                R.color.colorCall
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


