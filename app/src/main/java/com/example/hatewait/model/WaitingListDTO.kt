package com.example.hatewait.model

import java.sql.Date


data class WaitingListResponseData(var message:String, var waiting_customers:List<WaitingInfo>)
data class WaitingInfo(var phone:String, var name:String, var people_number: Int, var called_time: Date)


