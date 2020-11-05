package com.example.hatewait.retrofit2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyApi {

    private const val BASE_URL = "https://hatewait-server.herokuapp.com/"

    data class onlyMessageResponseData(var message:String)

    private fun retrofit(): Retrofit { // Singleton
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON
            .build()
    }



    val nonMemberRegisterService: RetrofitNonMemberRegister by lazy {
        retrofit().create(RetrofitNonMemberRegister::class.java)
    }

    val memberRegisterService: RetrofitMemberRegister by lazy {
        retrofit().create(RetrofitMemberRegister::class.java)
    }

    val checkMemberIdService: RetrofitCheckMemberIdRegister by lazy {
        retrofit().create(RetrofitCheckMemberIdRegister::class.java)
    }

    val SignUpService: RetrofitSignUp by lazy {
        retrofit().create(RetrofitSignUp::class.java)
    }

    val LoginService: RetrofitLogin by lazy {
        retrofit().create(RetrofitLogin::class.java)
    }

    val WaitingListService: RetrofitWaitingList by lazy {
        retrofit().create(RetrofitWaitingList::class.java)
    }

    val CouponService: RetrofitCoupon by lazy {
        retrofit().create(RetrofitCoupon::class.java)
    }

    val UpdateService: RetrofitInfoUpdate by lazy {
        retrofit().create(RetrofitInfoUpdate::class.java)
    }









}
