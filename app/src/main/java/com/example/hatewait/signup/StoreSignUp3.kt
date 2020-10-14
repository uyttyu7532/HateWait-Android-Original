package com.example.hatewait.signup

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.hatewait.R
import com.example.hatewait.address.AddressDialogFragment
import kotlinx.android.synthetic.main.activity_store_signup3.*
import java.io.IOException

// 1단계 이메일 , 인증번호 (네아로면 생략)
// 2단계 비번, 비번확인
// 3단계 가게이름, 전화번호, 도로명주소
// 4단계 가게 영업시간, 인원 수, 문구
// 가입완료 환영 메시지 액티비티 or 로그인바로됨

private lateinit var mContext: Context
lateinit var addressDialog: AddressDialogFragment

class StoreSignUp3 : AppCompatActivity(), AddressDialogFragment.AddressDialogListener {


    private val storeNameRegex = Regex("^(?=.*[a-zA-Z가-힣0-9])[a-zA-Z가-힣0-9|\\s|,]{1,}$")
    private val storePhoneRegex = Regex("^[0](\\d{2})(\\d{3,4})(\\d{3,4})")
    private val storeAddressRegex = Regex("^[가-힣]+[가-힣a-zA-Z0-9|\\-|,|\\s]{1,50}$")
    fun verifyName(storeName: String): Boolean = storeNameRegex.matches(storeName)
    fun verifyPhone(storePhone: String): Boolean = storePhoneRegex.matches(storePhone)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_signup3)
        setSupportActionBar(register_toolbar2)
        mContext = this.applicationContext


        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            setHomeActionContentDescription("아이디 & 비밀번호")
            setDisplayShowTitleEnabled(false)
        }
        addTextChangeListener()


        button_continue.setOnClickListener {
            val intent = Intent(this, StoreSignUp4::class.java)
            intent.putExtra("STORE_ID", getIntent().getStringExtra("USER_ID"))
            intent.putExtra("STORE_PASSWORD", getIntent().getStringExtra("USER_PASSWORD"))
            intent.putExtra("STORE_NAME", store_name_input_editText.text.toString())
            intent.putExtra("STORE_PHONE", store_phone_editText.text.toString())
//            intent.putExtra("STORE_ADDRESS", store_address_input_editText.text.toString())
            startActivity(intent)
        }


        store_address_input_edit_text.setOnClickListener {
            val bundle = Bundle()
            addressDialog = AddressDialogFragment().getInstance()
            addressDialog.arguments = bundle
            supportFragmentManager?.let { fragmentManager ->
                addressDialog.show(
                    fragmentManager,
                    "SELECT_ADDRESS"
                )

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.back_front_button_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.forward_button -> {
                if (!button_continue.isEnabled) {
                    return false
                } else {
                    button_continue.performClick()
                }
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    private fun addTextChangeListener() {
        // 가게명
        store_name_input_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!verifyName(s.toString())) {
                    store_name_input_layout.error =
                        resources.getString(R.string.store_name_error_message)
                    button_continue.isEnabled = false
                } else {
                    store_name_input_layout.error = null
                    store_name_input_layout.hint = null
                }

                button_continue.isEnabled =
                    store_name_input_layout.error == null
                            && store_phone_layout.error == null
                            && !store_phone_editText.text.isNullOrBlank()
//                            && store_address_input_editText.error == null
//                            && !store_address_input_editText.text.isNullOrBlank()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        // 전화번호
        store_phone_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(storePhone: Editable?) {
                if (!verifyPhone(storePhone.toString())) {
                    store_phone_layout.error =
                        resources.getString(R.string.store_phone_error_message)
                    button_continue.isEnabled = false
                } else {
                    store_phone_layout.error = null
                    store_phone_layout.hint = null
                }
                button_continue.isEnabled =
                    store_name_input_layout.error == null
                            && store_phone_layout.error == null
                            && !store_phone_editText.text.isNullOrBlank()
//                            && store_address_input_editText.error == null
//                            && !store_address_input_editText.text.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // 도로명주소
//        store_address_input_editText.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(storeAddress: Editable?) {
//                if (!verifyAddress(storeAddress.toString())) {
//                    store_address_input_layout.error = "하이픈(-)과 콤마(,) 제외한 특수문자는 허용되지않습니다."
//                    button_continue.isEnabled = false
//                } else {
//                    store_address_input_layout.error = null
//                    store_address_input_layout.hint = null
//                }
//                button_continue.isEnabled =
//                    (store_name_input_layout.error == null
//                            && store_phone_layout.error == null
//                            && !store_phone_editText.text.isNullOrBlank()
//                            && store_address_input_editText.error == null
//                            && !store_address_input_editText.text.isNullOrBlank())
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })

        lat_lon_button.setOnClickListener {


        var geocoder: Geocoder = Geocoder(this);

            var list: List<Address>? = null;

            var str: String = store_address_input_edit_text.text.toString();
            try {
                list = geocoder.getFromLocationName(
                    str, // 지역 이름
                    10
                ); // 읽을 개수
            } catch (e: IOException) {
                e.printStackTrace();
                Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
            }

            if (list != null) {
                if (list.isEmpty()) {
                    lat_lon_text_view.text = "해당되는 주소 정보는 없습니다";
                } else {
                    lat_lon_text_view.text = list[0].latitude.toString()+" "+list[0].longitude.toString();
                    //          list.get(0).getCountryName();  // 국가명
                    //          list.get(0).getLatitude();        // 위도
                    //          list.get(0).getLongitude();    // 경도
                }
            }
        }


    }

    override fun dismissDialog(dialog: DialogFragment) {
        Toast.makeText(mContext, "다이얼로그 닫고싶어요", Toast.LENGTH_SHORT).show()
        dialog.dismiss()
    }

}

