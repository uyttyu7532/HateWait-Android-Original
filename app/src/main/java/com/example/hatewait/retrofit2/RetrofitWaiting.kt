package com.example.hatewait.retrofit2

import com.example.hatewait.model.CallWaitingResponseData
import com.example.hatewait.model.DeleteWaitingResponseData
import com.example.hatewait.model.MyWaitingResponseData
import com.example.hatewait.model.WaitingListResponseData
import retrofit2.Call
import retrofit2.http.*

interface RetrofitWaiting {
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


    // 본인 대기 현황 조회
    @GET("/members/{memberId}/waiting-customers")
    fun requestMyWaiting(
        @Path("memberId") memberId: String
    ):
            Call<MyWaitingResponseData>


    // TODO 본인 대기 취소
    @POST("")
    fun requestCancelWaiting(
        @Path("memberId") memberId: String
    ):
            Call<MyApi.onlyMessageResponseData>

}




