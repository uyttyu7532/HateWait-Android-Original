package com.example.hatewait

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.util.Attributes
import java.util.*

    /**
     * RecyclerView: The new recycler view replaces the list view. Its more modular and therefore we
     * must implement some of the functionality ourselves and attach it to our recyclerview.
     * 1) Position items on the screen: This is done with LayoutManagers
     * 2) Animate & Decorate views: This is done with ItemAnimators & ItemDecorators
     * 3) Handle any touch events apart from scrolling: This is now done in our adapter's ViewHolder
     */
class StoreWaitingList : AppCompatActivity() {

        var client_list = ArrayList<ClientData>()
        lateinit var adapter: SwipeRecyclerViewAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_store_waiting_list)

            init()
        }


        private fun init() {

            readFile()
            setRecyclerView()

            val fab: View = findViewById(R.id.addFab)
            fab.setOnClickListener { view ->
                addDialog()
            }
        }


        // RecyclerView와 Adapter 연결
        private fun setRecyclerView(){
            // 나중에 디자인부분 DividerItemDecoration.java 참고
            // http://helloandroidexample.blogspot.com/2015/07/swipe-recyclerview-using.html

            var mRecyclerView = findViewById<View>(R.id.myRecyclerView) as RecyclerView
            // Layout Managers:
            mRecyclerView!!.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            // Item Decorator:
            // mRecyclerView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.divider)))
            // mRecyclerView.setItemAnimator(new FadeInLeftAnimator());

            // Creating Adapter object
            var mAdapter = SwipeRecyclerViewAdapter(client_list!!)

            //Single or Multiple
            mAdapter.mode = Attributes.Mode.Single
            mRecyclerView!!.adapter = mAdapter

            /* Scroll Listeners */
//            mRecyclerView!!.addOnScrollListener(object :
//                RecyclerView.OnScrollListener() {
//                override fun onScrollStateChanged(
//                    recyclerView: RecyclerView,
//                    newState: Int
//                ) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                    Log.e("RecyclerView", "onScrollStateChanged")
//                }
//
//                override fun onScrolled(
//                    recyclerView: RecyclerView,
//                    dx: Int,
//                    dy: Int
//                ) {
//                    super.onScrolled(recyclerView, dx, dy)
//                }
//            })
        }


        // 나중에 db에서 불러오기
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
                client_list?.add(data_tmp)
            }

        }

        private fun addDialog(){
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.activity_add_waiting, null)
            val builder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("대기 손님 추가")
            builder.setPositiveButton("추가"){
                    _,_->
            }
            builder.setNegativeButton("취소"){
                    _,_->
            }
        //            mDialogView.AddBtn.setOnClickListener {
        //                mAlertDialog.dismiss()
        //            }
        //            mDialogView.CancelAddBtn.setOnClickListener {
        //                mAlertDialog.dismiss()
        //            }
            val dig = builder.create()
            dig.show()
        }

            }
