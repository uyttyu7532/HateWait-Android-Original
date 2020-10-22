package com.example.hatewait.retrofit2

import com.example.hatewait.model.CustomerSignUpRequestData
import com.example.hatewait.model.CustomerSignUpResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitSignUp {
    @POST("/register/member")
    fun requestCustomerSignUp(
        @Body customerSignUp: CustomerSignUpRequestData
    ): Call<CustomerSignUpResponseData>
}
