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

    @PATCH("/stores/{store-id}/waiting-customers")
    fun requestWaitingCall(
        @Path("store-id") id: String,
        @Body phone: String
    ): Call<CallWaitingResponseData>

}




