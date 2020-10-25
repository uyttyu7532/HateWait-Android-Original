package com.example.hatewait.retrofit2

import com.example.hatewait.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitMemberRegister {
    @POST("/waiting-customers/bani123")
    fun requestMemberRegister(
        @Body memberRegister: MemberRegisterRequestData
    ): Call<MemberRegisterResponseData>
}

interface RetrofitNonMemberRegister {
    @POST("/waiting-customers/bani123")
    fun requestNonMemberRegister(
        @Body nonMemberRegister: NonMemberRegisterRequestData
    ): Call<NonMemberRegisterResponseData>
}

interface RetrofitCheckMemberIdRegister {
    @POST("/members")
    fun requestCheckMemberId(
        @Body memberId: CheckMemberIdRequestData
    ): Call<CheckMemberIdResponseData>
}




