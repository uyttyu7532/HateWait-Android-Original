package com.example.hatewait.retrofit2

import com.example.hatewait.model.CallWaitingResponseData
import com.example.hatewait.model.DeleteWaitingResponseData
import com.example.hatewait.model.WaitingListResponseData
import retrofit2.Call
import retrofit2.http.*

interface RetrofitWaitingList {
    @GET("/stores/{store-id}/waiting-customers")
    fun requestWaitingList(
        @Path("store-id") userId: String
    ):
            Call<WaitingListResponseData>

    @HTTP(method = "DELETE", path = "/stores/{store-id}/waiting-customers", hasBody = true)
    fun requestDeleteWaiting(
        @Path("store-id") userId: String,
        @Body deleteWaiting: DeleteWaitingResponseData
    )
            : Call<MyApi.onlyMessageResponseData>

    @FormUrlEncoded
    @PATCH("/stores/{store-id}/waiting-customers")
    fun requestWaitingCall(
        @Path("store-id") userId: String,
        @Field("phone") phone: String
    ): Call<CallWaitingResponseData>

}




