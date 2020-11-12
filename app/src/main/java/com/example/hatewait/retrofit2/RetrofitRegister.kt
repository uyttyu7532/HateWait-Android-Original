package com.example.hatewait.retrofit2

import com.example.hatewait.model.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitRegister {

    @POST("/stores/{store-id}/waiting-customers")
    fun requestMemberRegister(
        @Path("store-id") id: String,
        @Body memberRegister: MemberRegisterRequestData
    ): Call<MemberRegisterResponseData>


    @POST("/stores/{store-id}/waiting-customers")
    fun requestNonMemberRegister(
        @Path("store-id") id: String,
        @Body nonMemberRegister: NonMemberRegisterRequestData
    ): Call<NonMemberRegisterResponseData>

    @POST("/members")
    fun requestCheckMemberId(
        @Body memberId: CheckMemberIdRequestData
    ): Call<CheckMemberIdResponseData>
}




