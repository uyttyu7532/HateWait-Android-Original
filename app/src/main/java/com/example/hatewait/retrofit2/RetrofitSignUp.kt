package com.example.hatewait.retrofit2

import com.example.hatewait.model.MemberSignUpRequestData
import com.example.hatewait.model.MemberSignUpResponseData
import com.example.hatewait.model.StoreSignUpRequestData
import com.example.hatewait.model.StoreSignUpResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitSignUp {
    @POST("/register/member")
    fun requestCustomerSignUp(
        @Body memberSignUp: MemberSignUpRequestData
    ): Call<MemberSignUpResponseData>

    @POST("/register/store")
    fun requestStoreSignUp(
        @Body storeSignUp: StoreSignUpRequestData
    ): Call<StoreSignUpResponseData>
}
