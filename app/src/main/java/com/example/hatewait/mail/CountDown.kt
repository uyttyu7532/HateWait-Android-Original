package com.example.hatewait.mail

import android.app.Dialog
import android.os.CountDownTimer
import android.widget.TextView

fun countDown(time: String, checkTimer: TextView, dialog: Dialog) {
    var conversionTime: Long = 0

    // 1000 단위가 1초
    // 60000 단위가 1분
    // 60000 * 3600 = 1시간
    var getHour = time.substring(0, 2)
    var getMin = time.substring(2, 4)
    var getSecond = time.substring(4, 6)

    // "00"이 아니고, 첫번째 자리가 0 이면 제거
    if (getHour.substring(0, 1) === "0") {
        getHour = getHour.substring(1, 2)
    }
    if (getMin.substring(0, 1) === "0") {
        getMin = getMin.substring(1, 2)
    }
    if (getSecond.substring(0, 1) === "0") {
        getSecond = getSecond.substring(1, 2)
    }

    // 변환시간
    conversionTime =
        java.lang.Long.valueOf(getHour) * 1000 * 3600 + java.lang.Long.valueOf(getMin) * 60 * 1000 + java.lang.Long.valueOf(
            getSecond
        ) * 1000

    // 첫번쨰 인자 : 원하는 시간 (예를들어 30초면 30 x 1000(주기))
    // 두번쨰 인자 : 주기( 1000 = 1초)
    object : CountDownTimer(conversionTime, 1000) {
        // 특정 시간마다 뷰 변경
        override fun onTick(millisUntilFinished: Long) {

            // 시간단위
            var hour = (millisUntilFinished / (60 * 60 * 1000)).toString()

            // 분단위
            val getMin = millisUntilFinished - millisUntilFinished / (60 * 60 * 1000)
            var min = (getMin / (60 * 1000)).toString() // 몫

            // 초단위
            var second = (getMin % (60 * 1000) / 1000).toString() // 나머지

            // 밀리세컨드 단위
            val millis = (getMin % (60 * 1000) % 1000).toString() // 몫

            // 시간이 한자리면 0을 붙인다
            if (hour.length == 1) {
                hour = "0$hour"
            }

            // 분이 한자리면 0을 붙인다
            if (min.length == 1) {
                min = "0$min"
            }

            // 초가 한자리면 0을 붙인다
            if (second.length == 1) {
                second = "0$second"
            }
            checkTimer.setText("$hour:$min:$second")
        }

        override fun onFinish() {
//                Toast.makeText(mcontext, "인증시간이 종료되었습니다..", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }.start()
}