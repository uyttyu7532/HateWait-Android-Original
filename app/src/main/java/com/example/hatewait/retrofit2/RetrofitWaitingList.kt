package com.example.hatewait.retrofit2

import com.example.hatewait.model.WaitingListResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

//test안해봄
interface RetrofitWaitingList {
    @GET("/waiting-customers/{userId}")
    fun requestWaitingList(
        @Path("userId") userId: String?):
            Call<WaitingListResponseData>
}