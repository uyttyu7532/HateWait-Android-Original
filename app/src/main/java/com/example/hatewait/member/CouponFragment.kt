package com.example.hatewait.member

import LottieDialogFragment.Companion.newInstance
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hatewait.R
import com.example.hatewait.login.LoginInfo.memberInfo
import com.example.hatewait.model.CouponListInfo
import com.example.hatewait.model.CouponListResponseData
import com.example.hatewait.retrofit2.MyApi
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CouponFragment : Fragment(),
    DiscreteScrollView.OnItemChangedListener<CouponAdapter.ViewHolder?>,
    View.OnClickListener {

    private var couponList: List<CouponListInfo>? = null
    private var infiniteAdapter: InfiniteScrollAdapter<*>? = null
    private var rootView: View? = null
    private var itemPicker:DiscreteScrollView? = null
    private var storeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var bundle: Bundle = this.requireArguments()
        storeId = bundle.getString("store_id")

        Log.d("retrofit2", "$storeId")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_coupon, container, false)
        itemPicker = rootView!!.findViewById(R.id.coupon_item_picker) as DiscreteScrollView
        itemPicker!!.setOrientation(DSVOrientation.HORIZONTAL)
        itemPicker!!.addOnItemChangedListener(this)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        shop = Shop.get()
//        data = shop!!.data
//        itemPicker = view.findViewById<DiscreteScrollView>(R.id.item_picker)
//
//        itemPicker!!.setOrientation(DSVOrientation.HORIZONTAL)
//        itemPicker!!.addOnItemChangedListener(this)
//        infiniteAdapter = InfiniteScrollAdapter.wrap(ShopAdapter(data!!))
//        itemPicker!!.adapter = infiniteAdapter
//
//
//        itemPicker!!.setItemTransformer(
//            ScaleTransformer.Builder()
//                .setMaxScale(1.05f)
//                .setMinScale(0.8f)
//                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
//                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
//                .build()
//        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        newInstance().show(requireActivity().supportFragmentManager, "")
        MyApi.CouponService.requestCouponList(
            memberInfo!!.id,
            storeId!!
        )
            .enqueue(object : Callback<CouponListResponseData> {
                override fun onFailure(
                    call: Call<CouponListResponseData>,
                    t: Throwable
                ) {
                    Log.d("retrofit2 쿠폰 리스트 :: ", "연결실패 $t")
                }

                override fun onResponse(
                    call: Call<CouponListResponseData>,
                    response: Response<CouponListResponseData>
                ) {
                    newInstance().dismiss()
                    var data: CouponListResponseData? = response?.body() // 서버로부터 온 응답
                    Log.d(
                        "retrofit2 쿠폰 리스트 ::",
                        response.code().toString() + response.body().toString()
                    )
                    when (response.code()) {
                        200 -> {

                            couponList = data!!.coupons

                            infiniteAdapter = InfiniteScrollAdapter.wrap(CouponAdapter(data!!.coupons!!))
                            itemPicker!!.adapter = infiniteAdapter


                            itemPicker!!.setItemTransformer(
                                ScaleTransformer.Builder()
                                    .setMaxScale(1.05f)
                                    .setMinScale(0.8f)
                                    .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                                    .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                                    .build()
                            )
                        }
                    }
                }
            }
            )
    }

    override fun onClick(v: View) {
//        when (v.id) {
//
//            R.id.home -> finish()
//
////            else ->
//        }
    }


    override fun onCurrentItemChanged(viewHolder: CouponAdapter.ViewHolder?, adapterPosition: Int) {
        val positionInDataSet: Int = infiniteAdapter!!.getRealPosition(adapterPosition)
    }

}