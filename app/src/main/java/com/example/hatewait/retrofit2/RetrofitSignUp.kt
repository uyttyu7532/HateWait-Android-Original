package com.example.hatewait.retrofit2

import com.example.hatewait.model.MemberSignUpRequestData
import com.example.hatewait.model.MemberSignUpResponseData
import com.example.hatewait.model.StoreSignUpRequestData
import com.example.hatewait.model.StoreSignUpResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitSignUp {
    @POST("/register/member")
    fun requestCustomerSignUp(
        @Body memberSignUp: MemberSignUpRequestData
    ): Call<MemberSignUpResponseData>

    @POST("/register/store")
    fun requestStoreSignUp(
        @Body storeSignUp: StoreSignUpRequestData
    ): Call<StoreSignUpResponseData>

    // 손님 ID 중복 검사
    @GET("/register/members/id/{id}")
    fun requestCheckMemberId(
        @Path("id") userId: String
    ): Call<MyApi.onlyMessageResponseData>

    // 가게 ID 중복 검사
    @GET("/register/stores/id/{id}")
    fun requestCheckStoreId(
        @Path("id") userId: String
    ): Call<MyApi.onlyMessageResponseData>


}
