package com.example.hatewait.retrofit2

import com.example.hatewait.model.CustomerLoginResponseData
import com.example.hatewait.model.CustomerLoginRequestData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitLogin {
    @POST("/login/members/test")
    fun requestCustomerLogin(
        @Body customerLogin: CustomerLoginRequestData
    ): Call<CustomerLoginResponseData>
}
