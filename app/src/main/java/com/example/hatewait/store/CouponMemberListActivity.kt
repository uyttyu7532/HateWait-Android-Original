package com.example.hatewait.store

import CouponMemberViewAdapter
import LottieDialogFragment.Companion.fragment
import LottieDialogFragment.Companion.newInstance
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R
import com.example.hatewait.login.LoginInfo
import com.example.hatewait.model.CouponMember
import com.example.hatewait.model.CouponMemberListResponseData
import com.example.hatewait.retrofit2.MyApi
import kotlinx.android.synthetic.main.activity_coupon_member_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CouponMemberListActivity : AppCompatActivity() {

    lateinit var couponMemberListRecyclerView: RecyclerView
    lateinit var couponMemberListAdapter: CouponMemberViewAdapter
    var couponMember: List<CouponMember>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_member_list)


        setSupportActionBar(coupon_member_list_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setHomeActionContentDescription("로그인 화면 이동")
            setDisplayShowTitleEnabled(false)
        }


        couponMemberListRecyclerView = findViewById(
            R.id.coupon_member_list_recycler_view
        )


        setRecyclerView()


        coupon_member_search_view.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                CouponMemberViewAdapter.filter(newText)
                return false
            }
        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }


    // RecyclerView와 Adapter 연결
    private fun setRecyclerView() {

        couponMemberListRecyclerView!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        if (fragment == null || (!(fragment?.isAdded)!!)) {
            newInstance().show(supportFragmentManager, "")
        }
        MyApi.CouponService.requestCouponMemberList(
            LoginInfo.storeInfo.id
        ).enqueue(object : Callback<CouponMemberListResponseData> {
            override fun onFailure(
                call: Call<CouponMemberListResponseData?>,
                t: Throwable
            ) {
                Log.d("retrofit2 쿠폰회원 :: ", "연결실패 $t")
            }

            override fun onResponse(
                call: Call<CouponMemberListResponseData?>,
                response: Response<CouponMemberListResponseData?>
            ) {
                newInstance().dismiss()
                var data: CouponMemberListResponseData? = response?.body()
                Log.d(
                    "retrofit2 쿠폰회원 ::",
                    response.code().toString() + response.body().toString()
                )
                when (response.code()) {
                    200 -> {
                        couponMember = data!!.members
                        if (couponMember != null) {
                            couponMemberListAdapter =
                                CouponMemberViewAdapter(couponMember as ArrayList<CouponMember>)
                            couponMemberListRecyclerView.adapter = couponMemberListAdapter
                        }
                    }
                }
            }

        })

        try {
            if (coupon_member_search_view.query != null) {
                couponMemberListAdapter.filter.filter(coupon_member_search_view.query.toString())
            }
        } catch (_: Exception) {

        }
    }
}



