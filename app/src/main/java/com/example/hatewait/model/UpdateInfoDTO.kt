package com.example.hatewait.model

data class storeInfoData(
    var storeInfo: StoreInfoResponseData
)

data class StoreInfoResponseData(
    var id: String,
    var name: String,
    var phone: String,
    var email: String,
    var info: String,
    var business_hour: String,
    var maximum_capacity: Int,
    var address: String,
    var coupon_enable: Int
)


data class MemberInfoData(
    var memberInformation : MemberInfoResponseData
)

data class MemberInfoResponseData(
    var id: String,
    var name: String,
    var phone: String,
    var email: String,
    var no_show: Int
)

data class CouponUnableRequestData(
    var id: String,
    var coupon_enable: Boolean? = false
)

data class CouponEnableRequestData(
    var id: String,
    var coupon_enable: Boolean = true,
    var coupon_information :CouponInfoData
)

data class CouponInfoData(
    var benefit_description: String,
    var maximum_stamp: Int,
    var validity_period_days: Int,
    var remark: String
)

data class CouponResponseData(
    var couponInformation: CouponInfoData
)
