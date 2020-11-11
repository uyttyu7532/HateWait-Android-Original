package com.example.hatewait.retrofit2

import com.example.hatewait.model.MemberInfoData
import com.example.hatewait.model.storeInfoData
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInfoUpdate {

    @GET("stores/{id}")
    fun requestStoreInfo(
        @Path("id") storeID: String
    ): Call<storeInfoData>

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
        @Field("phone") phone: Int
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
    fun requestStorePassWordUpdate(
        @Field("id") id: String,
        @Field("pw") pw: String
    ): Call<MyApi.onlyMessageResponseData>


    // 손님
    @GET("members/{id}")
    fun requestMemberInfo(
        @Path("id") memberID: String
    ): Call<MemberInfoData>
}
