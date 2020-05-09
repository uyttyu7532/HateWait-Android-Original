package com.example.hatewait

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_store_waiting_list.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.split as split1

class StoreWaitingList : AppCompatActivity() {


    var client_list = ArrayList<ClientData>()
    lateinit var adapter: MyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_waiting_list)

        init()
    }



    @SuppressLint("WrongConstant")
    private fun init() {

        readFile()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        adapter = MyAdapter(client_list)
        adapter.itemClickListener = object:MyAdapter.onItemClickListener{
            override fun onItemClick(holder: MyAdapter.MyViewHolder, view: View, data: String, position: Int) {

                if(holder.detailView.visibility==GONE){
                    holder.detailView.visibility = VISIBLE
                }else{
                    holder.detailView.visibility = GONE
                }
            }

        }
        recyclerView.adapter = adapter
        val simpleCallback = object:ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                adapter.removeItem(viewHolder.adapterPosition)

            }


        }



        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)


        val fab: View = findViewById(R.id.addFab)
        fab.setOnClickListener { view ->
            addDialog()
        }


    }


    fun addDialog(){
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






    fun readFile(){
        val scan = Scanner(resources.openRawResource(R.raw.client))
        while(scan.hasNextLine()){
            val client = scan.nextLine()
            var id_tmp = client.split1(",")[0]
            var phone_tmp = client.split1(",")[1]
            var name_tmp = client.split1(",")[2]
            var people_num_tmp = client.split1(",")[3]
            var is_member_tmp = client.split1(",")[4]
            var data_tmp=ClientData(id_tmp,phone_tmp,name_tmp,people_num_tmp,is_member_tmp)
            client_list.add(data_tmp)
        }
    }



}



