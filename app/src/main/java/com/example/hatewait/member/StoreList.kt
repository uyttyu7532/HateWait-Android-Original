package com.example.hatewait.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R
import com.example.hatewait.model.NonMemberRegisterResponseData
import com.example.hatewait.model.StoreListResponseData
import com.example.hatewait.retrofit2.MyApi
import kotlinx.android.synthetic.main.activity_store_list.*
import kotlinx.android.synthetic.main.fragment_visitor_list.searchView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreList : AppCompatActivity() {

    lateinit var storeListRecyclerView: RecyclerView
    lateinit var storeListAdapter: StoreListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_list)

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
                storeListAdapter.filter.filter(newText)
                return false
            }
        })


    }


    // RecyclerView와 Adapter 연결
    private fun setRecyclerView() {

        storeListRecyclerView!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        var storeList = ArrayList<String>()
        storeList.add("고에몬")
        storeList.add("멘타이코")
        storeList.add("부타이")
        storeList.add("교촌치킨")
        storeList.add("엽기떡볶이")
        storeList.add("맛있는가게")
        storeList.add("갈비탕집")
        storeList.add("린슐랭")
        storeList.add("맛있는가게")
        storeList.add("마라탕집")

        MyApi.CouponService.requestStoreList(
            "uyttyu7532"
        )
            .enqueue(object : Callback<Any> {
                override fun onFailure(
                    call: Call<Any>,
                    t: Throwable
                ) {
                    Log.d("retrofit2 가게 리스트 :: ", "연결실패 $t")
                }

                override fun onResponse(
                    call: Call<Any>,
                    response: Response<Any>
                ) {
                    Log.d("retrofit2 가게 리스트 ::",response.code().toString() + response.body().toString())
                    when (response.code()) {
                        200 -> {
                            var data: Any? = response?.body() // 서버로부터 온 응답
                        }
                    }
                }
            }
            )

        storeListAdapter = StoreListAdapter(storeList)
        storeListRecyclerView.adapter = storeListAdapter

        try {
            if (searchView.query != null) {
                storeListAdapter.filter.filter(searchView.query.toString())
            }
        } catch (_: Exception) {

        }
    }
}