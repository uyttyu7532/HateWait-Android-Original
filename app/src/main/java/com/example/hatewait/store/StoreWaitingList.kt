package com.example.hatewait.store

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.daimajia.swipe.util.Attributes
import com.example.hatewait.R
import com.example.hatewait.model.ClientData
import com.example.hatewait.model.NewClient
import com.example.hatewait.model.getShared
import com.example.hatewait.socket.*
import kotlinx.android.synthetic.main.activity_signup1.*
import kotlinx.android.synthetic.main.activity_store_waiting_list.*
import org.jetbrains.anko.startActivity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


lateinit var waitingListRecyclerView: RecyclerView
var waitingList = ArrayList<ClientData>()
lateinit var waitingListAdapter: SwipeRecyclerViewAdapter
lateinit var waitingListContext: Context
lateinit var totalWaitingNumTextView: TextView


//lateinit var autoCallSwitchView: Switch
var called = HashMap<String, Boolean>()
lateinit var pref: SharedPreferences


class StoreWaitingList : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_waiting_list)

        setSupportActionBar(waiting_list_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setDisplayShowTitleEnabled(false)
        }

        waitingListContext = this.applicationContext
        pref = waitingListContext.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        waitingListRecyclerView = findViewById<View>(
            R.id.waiting_list_recycler_view
        ) as RecyclerView
        totalWaitingNumTextView = findViewById<View>(
            R.id.total_waiting_num_text_view
        ) as TextView
        waiting_list_coupon_button.setOnClickListener{
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
        Log.i("로그", "Resume")
        try {
            StoreWaitingListAsyncTask().execute()
        } catch (e: Exception) {
            Log.i("로그", e.toString())
        }
    }

    fun init() {
        setRecyclerView()
        makeAddDialog()
        readFile()

        waiting_list_refresh_button.setOnClickListener {
            waiting_list_refresh_button.visibility = INVISIBLE
            waiting_list_progress_bar.visibility = VISIBLE

            StoreWaitingListAsyncTask().execute()

            waiting_list_refresh_button.visibility = VISIBLE
            waiting_list_progress_bar.visibility = INVISIBLE
        }

        waiting_swipe_refresh.setOnRefreshListener {
            waiting_swipe_refresh.isRefreshing = true // progress bar 돌아가는 작업

            waiting_list_refresh_button.visibility = INVISIBLE
            waiting_list_progress_bar.visibility = VISIBLE

            StoreWaitingListAsyncTask().execute()

            waiting_list_refresh_button.visibility = VISIBLE
            waiting_list_progress_bar.visibility = INVISIBLE

            // 비동기에서 작업이 끝날때 swiperefresh.isRefreshing = false해줘야함
            waiting_swipe_refresh.isRefreshing = false
        }

//        autoCallBtn.setOnClickListener {
//            AutoCallAsyncTask().execute()
//        }


    }


    private fun readFile() {

        val scan = Scanner(resources.openRawResource(R.raw.client))
        while (scan.hasNextLine()) {
            val client = scan.nextLine()
            var id_tmp = client.split(",")[0]
            var phone_tmp = client.split(",")[1]
            var name_tmp = client.split(",")[2]
            var people_num_tmp = client.split(",")[3]
            var is_member_tmp = client.split(",")[4]
            var data_tmp =
                ClientData(
                    id_tmp,
                    phone_tmp,
                    name_tmp,
                    people_num_tmp,
                    is_member_tmp
                )
            waitingList?.add(data_tmp)
        }
        totalWaitingNumTextView.text = "현재 ${waitingList.size}팀 대기중"
    }


    private fun makeAddDialog() {
        val fab: View = findViewById(R.id.add_fab)
        fab.setOnClickListener { view ->

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
                positiveButton { dialog ->

                    if (addWaitingName.text.toString() == "" || addWaitingPerson.text.toString() == "" || addWaitingPhonenum.text.toString().equals("")
                    ) {
                        Toast.makeText(
                            waitingListContext,
                            "대기 손님 정보를 모두 입력해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        var newclient = NewClient(
                            name = addWaitingName.text.toString(),
                            peopleNum = addWaitingPerson.text.toString(),
                            phoneNum = addWaitingPhonenum.text.toString()
                        )

                        AddCustomerAsyncTask().execute(newclient)
                        StoreWaitingListAsyncTask().execute()

                        dismiss()
                    }
                }
                negativeButton() { dialog ->
                    dismiss()
                }
            }
        }
    }
}


// RecyclerView와 Adapter 연결
fun setRecyclerView() {

    waitingListRecyclerView!!.layoutManager =
        LinearLayoutManager(waitingListContext, LinearLayoutManager.VERTICAL, false)


    val prefKeys: MutableSet<String> = pref.all.keys
    for (pref_key in prefKeys) {
        if (getShared(pref, pref_key)) {
            called[pref_key] = true
        } else {
            called[pref_key] = false
        }
    }

    val clicked = HashMap<String, Boolean>()
    for (a in waitingList) {
        clicked[a.phone] = false
    }

    waitingListAdapter =
        SwipeRecyclerViewAdapter(
            waitingList!!,
            called,
            clicked,
            pref,
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




