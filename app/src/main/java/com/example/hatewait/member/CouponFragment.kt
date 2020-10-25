package com.example.hatewait.member


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hatewait.R
import com.example.hatewait.discretescroll.Coupon
import com.example.hatewait.discretescroll.Shop
import com.example.hatewait.discretescroll.ShopAdapter
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer

class CouponFragment : Fragment(), DiscreteScrollView.OnItemChangedListener<ShopAdapter.ViewHolder?>,
    View.OnClickListener  {

    private var data: List<Coupon>? = null
    private var shop: Shop? = null
    private var infiniteAdapter: InfiniteScrollAdapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView = inflater.inflate(R.layout.fragment_coupon, container, false)

        var recyclerView = rootView.findViewById(R.id.coupon_recycler_view) as RecyclerView
        var itemPicker = rootView.findViewById(R.id.coupon_item_picker) as DiscreteScrollView


        var couponTitleArray = ArrayList<String>()
        couponTitleArray.add("적립")
        couponTitleArray.add("소멸")
        couponTitleArray.add("사용")
        couponTitleArray.add("사용")
        couponTitleArray.add("적립")
        couponTitleArray.add("적립")
        couponTitleArray.add("소멸")
        couponTitleArray.add("사용")
        couponTitleArray.add("사용")
        couponTitleArray.add("적립")

        var couponDateArray = ArrayList<String>()
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")
        couponDateArray.add("2020-10-11 12:40")



        recyclerView.setHasFixedSize(true);
        var adapter = StampViewAdapter(couponTitleArray, couponDateArray)
        recyclerView.layoutManager =  LinearLayoutManager(activity)
        recyclerView.adapter = adapter


        shop = Shop.get()
        data = shop!!.data // 쿠폰 데이터

        itemPicker.setOrientation(DSVOrientation.HORIZONTAL)
        itemPicker.addOnItemChangedListener(this)
        infiniteAdapter = InfiniteScrollAdapter.wrap(ShopAdapter(data!!))
        itemPicker.adapter = infiniteAdapter


        itemPicker.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build()
        )


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

    override fun onClick(v: View) {
//        when (v.id) {
//
//            R.id.home -> finish()
//
////            else ->
//        }
    }


    override fun onCurrentItemChanged(viewHolder: ShopAdapter.ViewHolder?, adapterPosition: Int) {
        val positionInDataSet: Int = infiniteAdapter!!.getRealPosition(adapterPosition)
    }

}