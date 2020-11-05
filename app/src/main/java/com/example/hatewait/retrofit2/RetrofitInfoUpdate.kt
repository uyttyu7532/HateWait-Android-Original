package com.example.hatewait.retrofit2


import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.PATCH

interface RetrofitInfoUpdate {

    @FormUrlEncoded
    @PATCH("/stores/information")
    fun requestStoreNameUpdate(
        @Field("id") id: String,
        @Field("name") name: String
    ): Call<MyApi.onlyMessageResponseData>

    @FormUrlEncoded
    @PATCH("/stores/information")
    fun requestStorePhoneUpdate(
        @Field("id") id: String,
        @Field("phone") phone: String
    ): Call<MyApi.onlyMessageResponseData>

    @FormUrlEncoded
    @PATCH("/stores/information")
    fun requestStoreEmailUpdate(
        @Field("id") id: String,
        @Field("email") email: String
    ): Call<MyApi.onlyMessageResponseData>


    @FormUrlEncoded
    @PATCH("/stores/information")
    fun requestStoreInfoUpdate(
        @Field("id") id: String,
        @Field("info") info: String
    ): Call<MyApi.onlyMessageResponseData>

    @FormUrlEncoded
    @PATCH("/stores/information")
    fun requestStoreBusinessHourUpdate(
        @Field("id") id: String,
        @Field("business_hour") business_hour: String
    ): Call<MyApi.onlyMessageResponseData>

    @FormUrlEncoded
    @PATCH("/stores/information")
    fun requestStoreCapacityUpdate(
        @Field("id") id: String,
        @Field("maximum_capacity") maximum_capacity: Int
    ): Call<MyApi.onlyMessageResponseData>

    @FormUrlEncoded
    @PATCH("/stores/information")
    fun requestStoreAddressUpdate(
        @Field("id") id: String,
        @Field("address") address: String
    ): Call<MyApi.onlyMessageResponseData>

    @FormUrlEncoded
    @PATCH("/stores/information")
    fun requestStoreCouponEnableUpdate(
        @Field("id") id: String,
        @Field("coupon_enable") coupon_enable: Boolean
    ): Call<MyApi.onlyMessageResponseData>

    @FormUrlEncoded
    @PATCH("/stores/information")
    fun requestStoreCouponEnableUpdate(
        @Field("id") id: String,
        @Field("pw") pw: String
    ): Call<MyApi.onlyMessageResponseData>
}
