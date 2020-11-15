package com.example.hatewait.retrofit2

import com.example.hatewait.model.CouponListResponseData
import com.example.hatewait.model.CouponResponseData
import com.example.hatewait.model.StoreListResponseData
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitCoupon {

    // 가게 목록
    @SerializedName("array")
    @GET("/members/{memberId}/coupons")
    fun requestStoreList(
        @Path("memberId") memberId: String
    ):
            Call<StoreListResponseData>


    // 쿠폰
    @GET("/members/{memberId}/coupons/stores")
    fun requestCouponList(
        @Path("memberId") memberId: String,
        @Query("id") storeId: String
    ):
            Call<CouponListResponseData>


    // 가게에서 쿠폰 설정용
    @GET("/stores/{id}/coupon-information")
    fun requestStoreCouponInfo(
        @Path("id") id: String
    ): Call<CouponResponseData?>


    // 가게에서 쿠폰있는 손님 조회용
    @GET()
    fun requestCouponMemberList(

    ): Call<Any>
}


