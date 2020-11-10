package com.example.hatewait.retrofit2

import com.example.hatewait.model.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitRegister {

    @POST("/waiting-customers/{id}")
    fun requestMemberRegister(
        @Path("id") id: String,
        @Body memberRegister: MemberRegisterRequestData
    ): Call<MemberRegisterResponseData>


    @POST("/waiting-customers/{id}")
    fun requestNonMemberRegister(
        @Path("id") id: String,
        @Body nonMemberRegister: NonMemberRegisterRequestData
    ): Call<NonMemberRegisterResponseData>

    @POST("/members")
    fun requestCheckMemberId(
        @Body memberId: CheckMemberIdRequestData
    ): Call<CheckMemberIdResponseData>
}




