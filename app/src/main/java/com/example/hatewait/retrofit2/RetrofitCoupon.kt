package com.example.hatewait.retrofit2

import com.example.hatewait.model.CouponListResponseData
import com.example.hatewait.model.StoreListResponseData
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitCoupon {

    @SerializedName("array")
    @GET("/coupon/member/{memberId}")
    fun requestStoreList(
        @Path("memberId") memberId: String
    ):
            Call<StoreListResponseData>


    @GET("/coupon/member/{memberId}/store/{storeId}")
    fun requestCouponList(
        @Path("memberId") memberId: String,
        @Path("storeId") storeId: String
    ):
            Call<CouponListResponseData>
}


