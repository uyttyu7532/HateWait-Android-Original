package com.example.hatewait.model

data class CustomerLoginRequestData(var id:String, var pw:String)

data class CustomerLoginResponseData(var message:String, var member:String?)

//data class StoreLoginRequestData(var id:String, var pw:String)

//data class StoreLoginResponseData(var message:String, var store:String)