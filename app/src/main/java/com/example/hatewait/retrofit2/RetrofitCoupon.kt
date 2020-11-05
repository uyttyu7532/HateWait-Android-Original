package com.example.hatewait.retrofit2

import com.example.hatewait.model.StoreListResponseData
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitCoupon {
    @GET("/coupon/member/{memberId}")
    fun requestStoreList(
        @Path("memberId") memberId: String?
    ):
            Call<Any>


    @GET("/coupon/member/{memberId}/store/{storeId}")
    fun requestCouponList(
        @Path("memberId") memberId: String?,
        @Path("storeId") storeId: String?
    ):
            Call<StoreListResponseData>
}


