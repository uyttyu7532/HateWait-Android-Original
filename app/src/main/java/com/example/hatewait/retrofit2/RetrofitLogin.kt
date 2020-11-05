package com.example.hatewait.retrofit2

import com.example.hatewait.model.MemberLoginResponseData
import com.example.hatewait.model.MemberLoginRequestData
import com.example.hatewait.model.StoreLoginRequestData
import com.example.hatewait.model.StoreLoginResponseData

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitLogin {
    @POST("/login/members/test")
    fun requestMemberLogin(
        @Body memberLogin: MemberLoginRequestData
    ): Call<MemberLoginResponseData>

    @POST("/login/stores/test")
    fun requestStoreLogin(
        @Body storeLogin: StoreLoginRequestData
    ): Call<StoreLoginResponseData>
}


