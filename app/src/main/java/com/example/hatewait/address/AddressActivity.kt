package com.example.hatewait.address

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.webkit.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R


class AddressActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        result = findViewById(R.id.result)

        // WebView 초기화
        initWebView()

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = Handler()


    }


    private fun initWebView() {
        // WebView 설정
        val webView = findViewById<WebView>(R.id.webView)
        // JavaScript 허용
        webView.settings.javaScriptEnabled = true

        // JavaScript의 window.open 허용
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.loadWithOverviewMode = true // 웹뷰가 html 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정되도록 한다.
        webView.isHorizontalScrollBarEnabled = false
        webView.isVerticalScrollBarEnabled = false
        webView.setInitialScale(1)
//        webView.settings.builtInZoomControls = true
//        webView.settings.displayZoomControls = false
//		webView.setInitialScale(100)


//        webView.zoomOut()
        webView.settings.useWideViewPort = true // meta tag


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(AndroidBridge(), "TestApp")

        // web client 를 chrome 으로 설정
        webView.webChromeClient = WebChromeClient()

        // webview url load
        webView.loadUrl("http://uyttyu7532.cafe24.com/addressapi.php")
    }


    private inner class AndroidBridge() {
        @JavascriptInterface
        fun setAddress(arg1: String, arg2: String, arg3: String) {
            result.text = String.format("(%s) %s %s", arg1, arg2, arg3)
            // WebView를 초기화 하지않으면 재사용할 수 없음
            initWebView()
        }
    }
}

