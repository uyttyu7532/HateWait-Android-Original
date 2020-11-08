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

//data class MemberInfoResponseData(
//    var phone: String
//)