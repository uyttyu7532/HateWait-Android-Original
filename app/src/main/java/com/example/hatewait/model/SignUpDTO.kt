package com.example.hatewait.model

data class MemberSignUpRequestData(
    var id: String,
    var name: String,
    var phone: String,
    var email: String,
    var pw: String
)

data class MemberSignUpResponseData(var message: String, var memberName: String)

data class StoreSignUpRequestData(
    var id: String,
    var name: String,
    var phone: String,
    var email: String,
    var info: String?,
    var business_hour: String?,
    var maximum_capacity: Int,
    var address : String,
    var pw: String
)

data class StoreSignUpResponseData(var message: String, var name: String)

