package com.example.hatewait.store

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.daimajia.swipe.util.Attributes
import com.example.hatewait.R
import com.example.hatewait.login.mContext
import com.example.hatewait.login.storeInfo
import com.example.hatewait.model.*
import com.example.hatewait.retrofit2.MyApi
import kotlinx.android.synthetic.main.activity_store_waiting_list.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


lateinit var waitingListRecyclerView: RecyclerView
lateinit var waitingListAdapter: SwipeRecyclerViewAdapter
lateinit var waitingListContext: Context
lateinit var totalWaitingNumTextView: TextView


//var waitingList : List<WaitingInfo>? = null

//lateinit var autoCallSwitchView: Switch
//var called = HashMap<String, Boolean>()
//lateinit var pref: SharedPreferences


class StoreWaitingList : AppCompatActivity() {

    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_waiting_list)

        waitingListContext = this.applicationContext

        setSupportActionBar(waiting_list_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setDisplayShowTitleEnabled(false)
        }

//        pref = waitingListContext.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        waitingListRecyclerView = findViewById<View>(
            R.id.waiting_list_recycler_view
        ) as RecyclerView
        totalWaitingNumTextView = findViewById<View>(
            R.id.total_waiting_num_text_view
        ) as TextView
        waiting_list_coupon_button.setOnClickListener {
            startActivity<VisitorListActivity>()
        }
//        autoCallBtn = findViewById<View>(
//            R.id.auto_call_btn
//        ) as CardView
//        autoCallBtnText = findViewById<View>(
//            R.id.auto_call_btn_text
//        ) as TextView

//        // 임시로 여기에
//        //Get Firebase FCM token
//        FirebaseInstanceId.getInstance().instanceId
//            .addOnSuccessListener(this,
//                { instanceIdResult ->
//                    Log.i("현재 토큰: ", instanceIdResult.token)
//                })
//        getWaitingList()

        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        Log.i("대기 리스트", "Resume")
        try {
            getWaitingList()
//            StoreWaitingListAsyncTask().execute()
        } catch (e: Exception) {
            Log.i("대기 리스트", e.toString())
        }
    }

    fun init() {

//        setRecyclerView()
        makeAddDialog()
//        readFile()

        waiting_list_refresh_button.setOnClickListener {
            waiting_list_refresh_button.visibility = INVISIBLE
            waiting_list_progress_bar.visibility = VISIBLE

//            StoreWaitingListAsyncTask().execute()
            getWaitingList()

            waiting_list_refresh_button.visibility = VISIBLE
            waiting_list_progress_bar.visibility = INVISIBLE
        }

        waiting_swipe_refresh.setOnRefreshListener {
            waiting_swipe_refresh.isRefreshing = true // progress bar 돌아가는 작업

            waiting_list_refresh_button.visibility = INVISIBLE
            waiting_list_progress_bar.visibility = VISIBLE

//            StoreWaitingListAsyncTask().execute()
            getWaitingList()

            waiting_list_refresh_button.visibility = VISIBLE
            waiting_list_progress_bar.visibility = INVISIBLE

            // 비동기에서 작업이 끝날때 swiperefresh.isRefreshing = false해줘야함
            waiting_swipe_refresh.isRefreshing = false
        }

//        autoCallBtn.setOnClickListener {
//            AutoCallAsyncTask().execute()
//        }


    }


//    private fun readFile() {
//
//        val scan = Scanner(resources.openRawResource(R.raw.client))
//        while (scan.hasNextLine()) {
//            val client = scan.nextLine()
//            var id_tmp = client.split(",")[0]
//            var phone_tmp = client.split(",")[1]
//            var name_tmp = client.split(",")[2]
//            var people_num_tmp = client.split(",")[3]
//            var is_member_tmp = client.split(",")[4]
//            var data_tmp =
//                ClientData(
//                    id_tmp,
//                    phone_tmp,
//                    name_tmp,
//                    people_num_tmp,
//                    is_member_tmp
//                )
//            waitingList?.add(data_tmp)
//        }
//        totalWaitingNumTextView.text = "현재 ${waitingList.size}팀 대기중"
//    }


    // TODO 입력 데이터 확인해야 함 (비어있다는 것 말고도)
    private fun makeAddDialog() {
        val fab: View = findViewById(R.id.add_fab)
        fab.setOnClickListener {

            MaterialDialog(this).show {
                noAutoDismiss()
                title(text = "대기손님 추가")
                val customView = customView(R.layout.activity_add_waiting).getCustomView()
                val addWaitingName =
                    customView.findViewById(R.id.addWaitingName) as TextView
                var addWaitingPerson =
                    customView.findViewById(R.id.addWaitingPerson) as TextView
                var addWaitingPhonenum =
                    customView.findViewById(R.id.addWaitingPhonenum) as TextView
                positiveButton {

                    if (addWaitingName.text.toString() == "" || addWaitingPerson.text.toString() == "" || addWaitingPhonenum.text.toString() == ""
                    ) {
                        Toast.makeText(
                            waitingListContext,
                            "대기 손님 정보를 모두 입력해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
//                        var newclient = NewClient(
//                            name = addWaitingName.text.toString(),
//                            peopleNum = addWaitingPerson.text.toString(),
//                            phoneNum = addWaitingPhonenum.text.toString()
//                        )

                        var userPhone = addWaitingPhonenum.text.toString().toInt()
                        var userName = addWaitingName.text.toString()
                        var userPeopleNum = addWaitingPerson.text.toString().toInt()


                        var nonMemberRegisterData =
                            NonMemberRegisterRequestData(userPhone, userName, userPeopleNum, false)

                        MyApi.RegisterService.requestNonMemberRegister(
                            storeInfo!!.id,
                            nonMemberRegisterData
                        )
                            .enqueue(object : Callback<NonMemberRegisterResponseData> {
                                override fun onFailure(
                                    call: Call<NonMemberRegisterResponseData>,
                                    t: Throwable
                                ) {

                                    Log.d("비회원 대기 등록(리스트) :: ", "연결실패 $t")
                                }

                                override fun onResponse(
                                    call: Call<NonMemberRegisterResponseData>,
                                    response: Response<NonMemberRegisterResponseData>
                                ) {
                                    Log.d(
                                        "retrofit2 비회원 대기 등록 ::",
                                        response.code().toString() + response.body().toString()
                                    )
                                    when (response.code()) {
                                        200 -> {
                                            var data: NonMemberRegisterResponseData? =
                                                response?.body() // 서버로부터 온 응답
                                        }
                                    }
                                }
                            }
                            )

//                        AddCustomerAsyncTask().execute(newclient)
                        getWaitingList()
                        dismiss()
                    }
                }
                negativeButton() {
                    dismiss()
                }
            }
        }
    }


}

//test안해봄
fun getWaitingList() {

    Log.i("대기 리스트 :: ", "getWaitingList()")

    MyApi.WaitingListService.requestWaitingList(storeInfo!!.id)
        .enqueue(object : Callback<WaitingListResponseData> {
            override fun onFailure(call: Call<WaitingListResponseData>, t: Throwable) {

                Log.d("대기 리스트 :: ", "서버 연결 실패 $t")
            }

            override fun onResponse(
                call: Call<WaitingListResponseData>,
                response: Response<WaitingListResponseData>
            ) {
                Log.d(
                    "retrofit2 대기 리스트 ::",
                    response.code().toString() + response.body().toString()
                )
                when (response.code()) {
                    200 -> {
                        val data = response.body() // 서버로부터 온 응답
                        data?.let {
                            var responseMessage = it.message
                            if (it.waiting_customers != null) {
                                setRecyclerView(it.waiting_customers!!)
                            } else {
                                Toast.makeText(mContext, "현재 대기 인원이 없어요", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            }
        }
        )
}

// RecyclerView와 Adapter 연결
fun setRecyclerView(waitingList: List<WaitingInfo>) {

    waitingListRecyclerView!!.layoutManager =
        LinearLayoutManager(waitingListContext, LinearLayoutManager.VERTICAL, false)


//    val prefKeys: MutableSet<String> = pref.all.keys
//    for (pref_key in prefKeys) {
//        if (getShared(pref, pref_key)) {
//            called[pref_key] = true
//        } else {
//            called[pref_key] = false
//        }
//    }

    val clicked = HashMap<String, Boolean>()
    for (a in waitingList) {
        clicked[a.phone] = false
    }

    waitingListAdapter =
        SwipeRecyclerViewAdapter(
            waitingList,
            clicked,
            waitingListContext
        )
    waitingListAdapter.mode = Attributes.Mode.Single

    waitingListAdapter.itemClickListener = object :
        SwipeRecyclerViewAdapter.onItemClickListener {
        override fun onItemClick(
            holder: SwipeRecyclerViewAdapter.SimpleViewHolder,
            view: View,
            position: Int
        ) {
            Log.d("position", position?.toString())
            if (holder.waitingListDetailTextView.visibility == GONE) {
                holder.waitingListDetailTextView.visibility = VISIBLE
            } else {
                holder.waitingListDetailTextView.visibility = GONE
            }
        }
    }
    waitingListRecyclerView.adapter =
        waitingListAdapter
    totalWaitingNumTextView.text = "현재 ${waitingList.size}팀 대기중"
}








