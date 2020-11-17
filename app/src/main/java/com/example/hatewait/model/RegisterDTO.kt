package com.example.hatewait.model

data class MemberRegisterRequestData(var member_id:String, var people_number: String, var is_member:Boolean)

data class MemberRegisterResponseData(var message:String, var name:String, var count:Integer)

data class NonMemberRegisterRequestData(var phone: String, var name:String, var people_number: String, var is_member:Boolean)

data class NonMemberRegisterResponseData(var message:String, var count:Integer)

data class CheckMemberIdRequestData(var id:String)

data class CheckMemberIdResponseData(var memberName:String)
