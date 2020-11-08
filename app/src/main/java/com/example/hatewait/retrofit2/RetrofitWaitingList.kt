package com.example.hatewait.retrofit2

import com.example.hatewait.model.DeleteWaitingResponseData
import com.example.hatewait.model.WaitingListResponseData
import retrofit2.Call
import retrofit2.http.*

interface RetrofitWaitingList {
    @GET("/waiting-customers/{userId}")
    fun requestWaitingList(
        @Path("userId") userId: String
    ):
            Call<WaitingListResponseData>

    @HTTP(method = "DELETE", path = "/waiting-customers/{id}", hasBody = true)
    fun requestDeleteWaiting(
        @Path("id") userId: String,
        @Body deleteWaiting : DeleteWaitingResponseData
    )
            :Call<MyApi.onlyMessageResponseData>

}




