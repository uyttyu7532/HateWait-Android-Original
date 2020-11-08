package com.example.hatewait.register

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.hatewait.R
import com.example.hatewait.login.LoginRegisterViewPagerActivity
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_start_wait.*
import org.jetbrains.anko.startActivity
import java.net.URISyntaxException


const val Tag = "MainActivity"
lateinit var mSocket: Socket
lateinit var username: String
var users: Array<String> = arrayOf()

class StartWaitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_wait)

        start_waiting_button.setOnClickListener {
            startActivity<LoginRegisterViewPagerActivity>()
        }
    }
}


class MainActivity : AppCompatActivity() {
    private var socket: Socket? = null
    var btn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        socket!!.on("msg", onMessage)
        socket!!.connect()
        var startWaitingButton = findViewById<CardView>(R.id.start_waiting_button)
        startWaitingButton!!.setOnClickListener {
            socket!!.emit("msg", "hi")
            Log.e("send", "data")
        }
    }

    private val onMessage = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val data = args[0] as String
            Log.e("get", data)
            test_socket.text = data
        })
    }

    init {
        try {
            socket = IO.socket("서버 url")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }
}










