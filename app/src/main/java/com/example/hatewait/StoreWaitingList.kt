package com.example.hatewait

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.daimajia.swipe.util.Attributes
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_store_waiting_list.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class StoreWaitingList : AppCompatActivity() {

    var clientList = ArrayList<ClientData>()
    lateinit var mAdapter: SwipeRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_waiting_list)


        init()
    }


    @SuppressLint("WrongViewCast")
    private fun init() {



        readFile()
        setRecyclerView()
        makeAddDialog()



        autoCallSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //자동호출 on
            } else {
                //자동호출 off
            }
        })
    }


    fun makeAddDialog() {
        val fab: View = findViewById(R.id.addFab)
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
                positiveButton() { dialog ->
                    Log.d("이름", addWaitingName.text.toString())
                    Log.d("인원", addWaitingPerson.text.toString())
                    Log.d("전화번호", addWaitingPhonenum.text.toString())
                    //db에 addClient()
                    dismiss()
                    Toasty.success(view.context, "추가되었습니다", Toast.LENGTH_SHORT, true).show()
                    setRecyclerView()
                }
                negativeButton() { dialog ->
                    dismiss()
                }
            }
        }
    }


    // RecyclerView와 Adapter 연결
    private fun setRecyclerView() {
        // 나중에 디자인부분 DividerItemDecoration.java 참고
        // http://helloandroidexample.blogspot.com/2015/07/swipe-recyclerview-using.html

        val mRecyclerView = findViewById<View>(R.id.myRecyclerView) as RecyclerView
        // Layout Managers:
        mRecyclerView!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // Item Decorator:
        // mRecyclerView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.divider)))
        // mRecyclerView.setItemAnimator(new FadeInLeftAnimator());

        val pref = this.getSharedPreferences("myPref", Context.MODE_PRIVATE)


        val called = HashMap<String, Boolean>()
        val prefKeys: MutableSet<String> = pref.all.keys
        for (pref_key in prefKeys) {
            called[pref_key] = true
        }

        val clicked = HashMap<String, Boolean>()
        for(a in clientList){
            clicked[a.phone] = false
        }



        // Creating Adapter object
        mAdapter = SwipeRecyclerViewAdapter(clientList!!,called,clicked,pref,baseContext)

        //Single or Multiple
        mAdapter.mode = Attributes.Mode.Single

//        mAdapter.itemClickListener = object : SwipeRecyclerViewAdapter.onItemClickListener {
//            override fun onItemClick(
//                holder: SwipeRecyclerViewAdapter.SimpleViewHolder,
//                view: View,
//                position: Int
//            ) {
//                Log.d("position", position?.toString())
//                if (holder.detailView.visibility == GONE) {
//                    holder.detailView.visibility = VISIBLE
//                } else {
//                    holder.detailView.visibility = GONE
//                }
//            }
//        }
        mRecyclerView.adapter = mAdapter

        /* Scroll Listeners */
//        mRecyclerView!!.addOnScrollListener(object :
//            RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(
//                recyclerView: RecyclerView,
//                newState: Int
//            ) {
//                super.onScrollStateChanged(recyclerView, newState)
//                Log.e("RecyclerView", "onScrollStateChanged")
//            }
//
//            override fun onScrolled(
//                recyclerView: RecyclerView,
//                dx: Int,
//                dy: Int
//            ) {
//                super.onScrolled(recyclerView, dx, dy)
//            }
//        })
    }

    // 나중에 db에서 불러오기
    // getList()
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
                ClientData(id_tmp, phone_tmp, name_tmp, people_num_tmp, is_member_tmp)
            clientList?.add(data_tmp)
        }

    }

}
