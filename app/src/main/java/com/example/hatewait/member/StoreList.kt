package com.example.hatewait.member

import LottieDialogFragment.Companion.newInstance
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R
import com.example.hatewait.login.LoginInfo.memberInfo
import com.example.hatewait.model.StoreListInfo
import com.example.hatewait.model.StoreListResponseData
import com.example.hatewait.retrofit2.MyApi
import kotlinx.android.synthetic.main.activity_store_list.*
import kotlinx.android.synthetic.main.fragment_visitor_list.searchView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

lateinit var StoreListContext: Context

class StoreList : AppCompatActivity() {


    lateinit var storeListRecyclerView: RecyclerView
    lateinit var storeListAdapter: StoreListAdapter
    var storeList: List<StoreListInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_list)

        StoreListContext = this

        storeListRecyclerView = findViewById<View>(
            R.id.store_recycler_view
        ) as RecyclerView

        setRecyclerView()


        store_list_search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                storeListAdapter.filter.filter(newText)
                return false
            }
        })


    }


    // RecyclerView와 Adapter 연결
    private fun setRecyclerView() {

        storeListRecyclerView!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        newInstance().show(supportFragmentManager, "")
        MyApi.CouponService.requestStoreList(
            memberInfo!!.id
        )
            .enqueue(object : Callback<StoreListResponseData> {
                override fun onFailure(
                    call: Call<StoreListResponseData>,
                    t: Throwable
                ) {
                    Log.d("retrofit2 가게 리스트 :: ", "연결실패 $t")
                }

                override fun onResponse(
                    call: Call<StoreListResponseData>,
                    response: Response<StoreListResponseData>
                ) {

                    newInstance().dismiss()

                    var data: StoreListResponseData? = response?.body() // 서버로부터 온 응답
                    Log.d(
                        "retrofit2 가게 리스트 ::",
                        response.code().toString() + response.body().toString()
                    )
                    when (response.code()) {
                        200 -> {

                            storeList = data!!.array
                            if (storeList != null) {
                                storeListAdapter =
                                    StoreListAdapter(storeList as ArrayList<StoreListInfo>)
                                storeListRecyclerView.adapter = storeListAdapter
                            }
                        }
                    }
                }
            }
            )


        try {
            if (searchView.query != null) {
                storeListAdapter.filter.filter(searchView.query.toString())
            }
        } catch (_: Exception) {

        }
    }
}