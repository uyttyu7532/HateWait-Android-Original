package com.example.hatewait.socket

import java.io.*
import java.net.Socket


const val SERVERIP: String = "1.241.5.154"
const val PORT = 3000// port num
var STOREID = "s0000"
var CUSTOMERID = "m0001"
var AUTONUM:Int? = 0
var AUTOCALL= true

var clientSocket: Socket? = null
var reader: BufferedReader? = null // 서버 < 앱
var writer: PrintWriter? = null // 앱 > 서버