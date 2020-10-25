package com.example.hatewait.model

data class MemberRegisterRequestData(var id:String, var people_number:Integer, var is_member:Boolean)

data class MemberRegisterResponseData(var message:String, var name:String, var count:Integer)

data class NonMemberRegisterRequestData(var phone:Integer, var name:String, var people_number:Integer, var is_member:Boolean)

data class NonMemberRegisterResponseData(var message:String, var count:Integer)

data class CheckMemberIdRequestData(var id:String)

data class CheckMemberIdResponseData(var message:String)

//data class StoreLoginRequestData(var id:String, var pw:String)

//data class StoreLoginResponseData(var message:String, var store:String)