package com.example.hatewait

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bashizip.bhlib.BusinessHours
import com.bashizip.bhlib.ValdationException
import kotlinx.android.synthetic.main.activity_business_hour_pick.*
import java.io.Serializable

class BusinessHourPick : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_hour_pick)

        init()
    }

    fun init() {
        var bhs: List<BusinessHours>? = null
        cancel_button.setOnClickListener {
            finish()
        }

        btn_apply.setOnClickListener {
//            bhs: List<BusinessHours?>? = null
            bhs = try {
                bh_picker.businessHoursList
            } catch (e: ValdationException) {
//                on 상태에서 영업시간을 입력하지 않은경우
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
//            val intent =
//                Intent(this, MainActivity::class.java)
//            intent.putExtra("BUSINESS_HOUR_LIST", bhs as Serializable?)
//            startActivity(intent)

//            영업시간을 입력한 경우에만 영업시간 확인 다이얼로그 프래그먼트 출력
            if (!bhs.isNullOrEmpty()) {
                val result = parsingBusinessHour(bhs!!)
//            result: 오전 1:00 - 오전 2:00 (휴무일 : 월요일, 목요일)
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                val businessTimeCheckFragment = BusinessHourCheckDialog()
                val argumentBundle = Bundle()
                argumentBundle.putString("NEW_BUSINESS_HOURS", result)
                businessTimeCheckFragment.arguments = argumentBundle
                businessTimeCheckFragment.show(this.supportFragmentManager, "BUSINESS_TIME_CHECK")
            }
        }

    }
    private fun parsingBusinessHour(businessHourList : List<BusinessHours>) : String {
//        맨 첫번째 원소 시작-끝시간 담음
        var businessTimeRange = "${businessHourList[0].from} - ${businessHourList[0].to}"
        var holidays = arrayListOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")
        var alwaysSameTimeBusiness = true
//        첫 영업 시간 시작-끝시간으로 초기화
        var businessTimeRangeArray : ArrayList<String> = arrayListOf(businessTimeRange)

//      일주일 - 영업일 = 휴무일
        for (day in businessHourList) {
            holidays.remove(day.dayOfWeek)
        }

//        매일 영업시간이 같은지 체크
        for (day in businessHourList) {
                if (businessTimeRangeArray.contains("${day.from} - ${day.to}")) continue
                else {
                    alwaysSameTimeBusiness = false
                    break
                }
        }
//        매일 같은 영업시간이면
        if (alwaysSameTimeBusiness) {
            return if(holidays.isEmpty()) {
//            휴일 없이 모든 날짜 영업시
                "매일 $businessTimeRange"
            } else {
                val holiday = holidays.toString().removePrefix("[").removeSuffix("]")
                "$businessTimeRange (휴무일 : ${holiday})"
            }

        } else {
            //            하루라도 영업시간 다르면 영업요일별 근무시간 표시
            var resultBusinessTimeString = ""
            for (day in businessHourList) {
                resultBusinessTimeString += "${day.dayOfWeek} : ${day.from} - ${day.to}\n"
            }
            return if (holidays.isEmpty()) {
                resultBusinessTimeString
            } else {
                val holiday = holidays.toString().removePrefix("[").removeSuffix("]")
                "$resultBusinessTimeString (휴무일 : ${holiday})"
            }

        }




    }
}
