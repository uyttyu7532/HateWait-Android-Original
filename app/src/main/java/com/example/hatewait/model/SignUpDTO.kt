package com.example.hatewait.model

data class CustomerSignUpRequestData(var id:String, var name:String, var phone:String, var email:String, var pw:String)
data class CustomerSignUpResponseData(var message:String, var memberName:String)

//data class StoreSignUpRequestData(var id:String, var name:String, var phone:String, var email:String, var pw:String)
//data class StoreSignUpResponseData(var message:String, var storeName:String)