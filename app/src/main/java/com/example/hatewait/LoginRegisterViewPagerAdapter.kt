package com.example.hatewait

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_members_register.*
import kotlinx.android.synthetic.main.activity_register_tab_pager.*
import org.jetbrains.anko.startActivity


class LoginRegisterViewPagerActivity : AppCompatActivity(), RegisterErrorDialogFragment.RegisterMemberDialogListener, NameCheckDialogFragment.NameCheckListener, MemberRegister.CustomerInfoListener {

    lateinit var newCustomerName : String
    var newCustomerTurn = -1
    private val tabNameArray = arrayOf<String>("비회원", "회원")
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_tab_pager)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()

    }
    private fun init() {
        view_pager.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(kind_of_member_tab_layout, view_pager) {
                tab, position ->
            tab.text = tabNameArray[position]
        }.attach()
        //attach() :    Link the TabLayout and the ViewPager2 together.
        // Must be called after ViewPager2 has an adapter set

    }
    override fun onDialogPositiveClick(dialog: DialogFragment) {

//        여기에서 asynctask 수행 방법?
        startActivity<RegisterCheck>(
            "CUSTOMER_NAME" to newCustomerName,
            "CUSTOMER_TURN" to newCustomerTurn
        )
        dialog.dismiss()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(this, "아이디를 확인헤주세요!", Toast.LENGTH_SHORT).show()
        dialog.dismiss()
    }

    override fun applyText(memberId: String) {
        user_id_input_editText.setText(memberId)
    }

    override fun registerCustomer(memberRegister: MemberRegister) {
        newCustomerName = memberRegister.customerName!!
        newCustomerTurn = memberRegister.customerTurn!!
    }


}
