package com.example.hatewait.socket

import java.io.BufferedReader
import java.io.PrintWriter
import java.net.Socket

val SERVERIP: String ="192.168.56.1"
val PORT = 3000// port num
var STOREID = "s0000"

var clientSocket: Socket? = null
var reader: BufferedReader? = null // 서버 < 앱
var writer: PrintWriter? = null // 앱 > 서버

