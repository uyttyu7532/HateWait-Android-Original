package com.example.hatewait.socket

import java.io.*
import java.net.Socket


const val SERVERIP: String = "192.168.35.244"
const val PORT = 3000// port num
var STOREID = "s0000"
var STORENAME = "가게이름"
var CUSTOMERID = "m0001"
var AUTONUM:Int? = 3
//var AUTOCALL= true

var clientSocket: Socket? = null
var reader: BufferedReader? = null // 서버 < 앱
var writer: PrintWriter? = null // 앱 > 서버