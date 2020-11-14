package com.example.hatewait.model

import java.util.*

data class WaitingListResponseData(var message:String?, var waiting_customers:List<WaitingInfo>?)
data class WaitingInfo(var phone:String, var name:String, var people_number: Int, var called_time: Date?)

data class DeleteWaitingResponseData(var phone:String, var visited: Boolean)

data class CallWaitingResponseData(var message:String, var called_time: Date?)

data class MyWaitingResponseData(var message: String?, var store_name :String?, var member_name : String?, var turn_number : Int?)

