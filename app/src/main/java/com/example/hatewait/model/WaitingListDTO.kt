package com.example.hatewait.model



data class WaitingListResponseData(var message:String, var waiting_customers:List<WaitingInfo>)
data class WaitingInfo(var phone:String, var name:String, var people_number: Int, var is_called: Int)


