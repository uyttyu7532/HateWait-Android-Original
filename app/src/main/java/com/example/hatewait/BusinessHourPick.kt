package com.example.hatewait

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.bashizip.bhlib.BusinessHours
import com.bashizip.bhlib.ValdationException
import kotlinx.android.synthetic.main.activity_business_hour_pick.*
import java.io.Serializable

class BusinessHourPick : AppCompatActivity(), BusinessHourCheckDialog.TimeCheckListener {

    private lateinit var updatedBusinessTime : String

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
                updatedBusinessTime = parsingBusinessHour(bhs!!)
//            result: 오전 1:00 - 오전 2:00 (휴무일 : 월요일, 목요일)
                Toast.makeText(this, updatedBusinessTime, Toast.LENGTH_SHORT).show()
                val businessTimeCheckFragment = BusinessHourCheckDialog()
                val argumentBundle = Bundle()
                argumentBundle.putString("NEW_BUSINESS_HOURS", updatedBusinessTime)
                businessTimeCheckFragment.arguments = argumentBundle
                businessTimeCheckFragment.show(this.supportFragmentManager, "BUSINESS_TIME_CHECK")
            }
        }

    }

    private fun parsingBusinessHour(businessHourList : List<BusinessHours>) : String {
//        맨 첫번째 원소 시작-끝시간 담음
        var businessTimeRange = if (businessHourList[0].from != resources.getString(R.string.all_day)) {
                                    "${businessHourList[0].from} - ${businessHourList[0].to}"
                                    } else {
                                        resources.getString(R.string.all_day)
                                    }
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
//            24시간이 아닌 경우
            if (day.from != resources.getString(R.string.all_day)) {
                if (!businessTimeRangeArray.contains("${day.from} - ${day.to}")) {
                    alwaysSameTimeBusiness = false
                    break
                }
            }
//            24시간인 경우
            else {
                if (!businessTimeRangeArray.contains(day.from)) {
                    alwaysSameTimeBusiness = false
                    break
                }
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
//                24시가 아니면 from to 형식
                resultBusinessTimeString +=
                    if(day.from != resources.getString(R.string.all_day)) {
                        "${day.dayOfWeek} : ${day.from} - ${day.to}\n"
                    } else {
//                    24시면 24시 표시
                    "${day.dayOfWeek} : ${day.from}\n"
                    }
            }

//            휴무일 없는 경우 영업시간만 표시
            return if (holidays.isEmpty()) {
                resultBusinessTimeString
            } else {
//                휴무일 있는경우 요일별 영업시간 (휴무일: 토요일, 일요일) 형식 표시
                val holiday = holidays.toString().removePrefix("[").removeSuffix("]")
                "$resultBusinessTimeString (휴무일 : ${holiday})"
            }

        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
//        변경 버튼 클릭시 원래 액티비티에 결과 적용
        dialog.dismiss()
        val intent = Intent()
        intent.putExtra("UPDATED_BUSINESS_TIME", updatedBusinessTime)
        setResult(200, intent)
        this@BusinessHourPick.finish()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
//
    }
}
