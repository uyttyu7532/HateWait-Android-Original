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
import android.os.AsyncTask
import android.util.Log
import com.example.hatewait.fcm.FcmPush
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets
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
//    private val mSelectedCallItems = SparseBooleanArray(0)
//    private val mSelectedDetailItems = SparseBooleanArray(0)

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

    //  ViewHolder Class
    inner class SimpleViewHolder(itemView: View) :

        RecyclerView.ViewHolder(itemView) {


        val swipeLayout = itemView.findViewById(R.id.swipeLayout) as SwipeLayout
        val clientView = itemView.findViewById(R.id.clientView) as CardView
        val clientNameView = itemView.findViewById(R.id.clientNameView) as TextView
        val clientNumView = itemView.findViewById(R.id.clientNumView) as TextView
        val clientPhoneView = itemView.findViewById(R.id.clientPhoneView) as TextView
        val detailView = itemView.findViewById(R.id.detailView) as CardView
        val detailView1 = itemView.findViewById(R.id.detailView1) as TextView
        val detailView2 = itemView.findViewById(R.id.detailView2) as TextView
        val delBtn = itemView.findViewById(R.id.delBtn) as ImageButton
        val bottom_wrapper_left = itemView.findViewById(R.id.bottom_wrapper_left) as FrameLayout
        val callBtn = itemView.callBtn


        init {

            callBtn.setOnClickListener { v ->
                val position = adapterPosition
                if (called.containsKey(items[position].phone) && called[items[position].phone]!!) {
                    setShared(pref, items[position].phone, false)
                    called[items[position].phone] = false
                    this.bottom_wrapper_left.backgroundColorResource = R.color.white
                    Toasty.error(itemView.context, items[position].phone, Toast.LENGTH_SHORT, true)
                        .show()
                } else {
                    setShared(pref, items[position].phone, true)
                    called[items[position].phone] = true
                    this.bottom_wrapper_left.backgroundColorResource = R.color.colorCall
                    Toasty.warning(
                        itemView.context,
                        items[position].phone,
                        Toast.LENGTH_SHORT,
                        true
                    ).show()


                    //푸시를 받을 유저의 UID가 담긴 destinationUid 값을 넣어준후 fcmPush클래스의 sendMessage 메소드 호출
                    val fcmPush = FcmPush()
                    fcmPush?.sendMessage(context)
                    //fcmPush?.sendMessage("fiARZ0G9lxs:APA91bENjxB-zasfoMSaD3cfUl-d5wWFS9E50NcuSv6c91WWDXxLJl5-SV_tDEu8aHP3AgR_gTPmQVhW_k6yW73wxd2aVK_bn2n1h-8e-27qp7OiN-qcIKOkJZk94Hwuvqfs_KaKZSRj", "알림 메세지 입니다.", message)

                    // 서버쪽에서 문자메시지 보내기
                    callCustomerMenuAsyncTask().execute()
                }
            }

//            clientView.setOnClickListener { v ->
//                val position = adapterPosition
//                if (clicked.containsKey(items[position].phone) && clicked[items[position].phone]!! ) {
//                    clicked[items[position].phone] = false
//                    detailView.visibility = View.GONE
//
//                }else{
//
//                    clicked[items[position].phone] = true
//
//                    detailView.visibility = View.VISIBLE
//                }
//            }


        }
    }


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
            delCustomerTask().execute()
            mItemManger.removeShownLayouts(viewHolder.swipeLayout)
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
            mItemManger.closeAllItems()

            Toasty.error(view.context, "삭제되었습니다", Toast.LENGTH_SHORT, true).show()
        }

        if (called.containsKey(items[position].phone) && called[items[position].phone]!!) {
            viewHolder.bottom_wrapper_left.backgroundColorResource = R.color.colorCall
        } else {
            viewHolder.bottom_wrapper_left.backgroundColorResource = R.color.white
        }

//        if (clicked.containsKey(items[position].phone) && clicked[items[position].phone]!! ) {
//            viewHolder.detailView.visibility = View.VISIBLE
//        } else {
//            viewHolder.detailView.visibility = View.GONE
//        }


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position)
    }


    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipeLayout
    }


}

class delCustomerTask : AsyncTask<Unit, Unit, Unit>() {

    private var clientSocket: Socket? = null
    private var reader: BufferedReader? = null // 서버 < 앱
    private var writer: PrintWriter? = null // 앱 > 서버

    private val port = 3000// port num
//    private val ip: String = "192.168.1.166"// 서버 ip적기


    override fun doInBackground(vararg params: Unit) { // 소켓 연결
        try {
            clientSocket = Socket(ip, port)
            Log.i("로그", "delCustomerTask:: ok")
            writer = PrintWriter(clientSocket!!.getOutputStream(), true)
            reader = BufferedReader(
                InputStreamReader(
                    clientSocket!!.getInputStream(),
                    StandardCharsets.UTF_8
                )
            )
            // TODO 삭제할 명단 :::: DELQUE;가게 id;손님id
            writer!!.println("DELQUE;s0000;n0000")
            Log.i("로그: 대기 삭제 서버로 보냄", "DELQUE;가게 id;손님(회원) id")
            val addCustomerResponse: String = reader!!.readLine()
            Log.i("로그: del서버응답", addCustomerResponse)


            if (reader != null) {
                try {
                    reader!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (writer != null) {
                writer!!.close()
            }
            if (clientSocket != null) {
                try {
                    clientSocket!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}


class callCustomerMenuAsyncTask : AsyncTask<Unit, Unit, Unit>() {

    private var clientSocket: Socket? = null
    private var reader: BufferedReader? = null // 서버 < 앱
    private var writer: PrintWriter? = null // 앱 > 서버

    private val port = 3000// port num
//    private val ip: String = "192.168.1.166"// 서버 ip적기


    override fun doInBackground(vararg params: Unit) { // 소켓 연결
        try {
            clientSocket = Socket(ip, port)
            Log.i("로그", "storeMenuAsyncTask:: ok")
            writer = PrintWriter(clientSocket!!.getOutputStream(), true)
            reader = BufferedReader(
                InputStreamReader(
                    clientSocket!!.getInputStream(),
                    StandardCharsets.UTF_8
                )
            )

            // TODO 문자를 받을 손님 id
            // writer!!.println("PUSHMSG;enu")
            Log.i("로그:서버에게 정보 달라고 보냄", "PUSHMSG;enu")
            val storeMenuResponse: String = reader!!.readLine()
            Log.i("로그:서버응답", storeMenuResponse)
            //서버>앱: MAIN;STORE;storeName;waitingNum;nextName;nextNum
            if (reader != null) {
                try {
                    reader!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (writer != null) {
                writer!!.close()
            }
            if (clientSocket != null) {
                try {
                    clientSocket!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}