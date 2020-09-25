package com.example.hatewait.address


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.hatewait.R
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.activity_store_signup3.*


class AddressDialogFragment : DialogFragment() {
    private val mHandler: Handler = Handler()
    var customView: View? = null

    lateinit var webView: WebView

    private val result: TextView by lazy {
        customView?.findViewById<TextView>(R.id.result)!!
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            customView = inflater.inflate(R.layout.activity_address, null)
            builder.setView(customView)
            val dialog = builder.create()
            dialog
        } ?: throw IllegalStateException("Activity Can't be null")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return customView
    }

    //onCreateView에서 넘겨준 customView를 넘겨받음.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWebView()
    }

    override fun onDestroyView() {
        customView = null
        super.onDestroyView()
    }

    fun getInstance(): AddressDialogFragment {
        return AddressDialogFragment()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initWebView() {

        webView = view!!.findViewById(R.id.webView)

        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.loadWithOverviewMode = true // 웹뷰가 html 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정되도록 한다.
        webView.isHorizontalScrollBarEnabled = false
        webView.isVerticalScrollBarEnabled = false
        webView.setInitialScale(1)
        webView.settings.useWideViewPort = true // meta tag


        webView.setOnTouchListener { v, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> if (!v.hasFocus()) {

                    mHandler.post {
                        val inputMethodManager =
                            context!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                        inputMethodManager!!.toggleSoftInputFromWindow(
                            edit.applicationWindowToken,
                            InputMethodManager.SHOW_FORCED,
                            0
                        )
                    }
                }
            }
            false
        }


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(AndroidBridge(), "TestApp")

        // web client 를 chrome 으로 설정
        webView.webChromeClient = WebChromeClient()


        // webview url load
        webView.loadUrl("http://uyttyu7532.cafe24.com/addressapi.php")

    }


    private inner class AndroidBridge {
        @JavascriptInterface
        fun setAddress(arg1: String, arg2: String, arg3: String) {
            result.text = String.format("(%s) %s %s", arg1, arg2, arg3)
            store_address_input_text.text = String.format("(%s) %s %s", arg1, arg2, arg3)
            // WebView를 초기화 하지않으면 재사용할 수 없음
            initWebView()

        }
    }
}

