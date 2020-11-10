package com.example.hatewait.model

data class MemberRegisterRequestData(var id:String, var people_number: Int, var is_member:Boolean)

data class MemberRegisterResponseData(var message:String, var name:String, var count:Integer)

data class NonMemberRegisterRequestData(var phone: Int, var name:String, var people_number: Int, var is_member:Boolean)

data class NonMemberRegisterResponseData(var message:String, var count:Integer)

data class CheckMemberIdRequestData(var id:String)

data class CheckMemberIdResponseData(var message:String)
