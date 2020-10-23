package com.example.hatewait.discretescroll

import android.accessibilityservice.GestureDescription
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.hatewait.R
import com.example.hatewait.discretescroll.Shop.Companion.get
import com.example.hatewait.member.ManageStampCouponActivity
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import org.jetbrains.anko.startActivity


class ShopActivity : AppCompatActivity(),
    DiscreteScrollView.OnItemChangedListener<ShopAdapter.ViewHolder?>, View.OnClickListener {
    private var data: List<Item>? = null
    private var shop: Shop? = null
    private var itemPicker: DiscreteScrollView? = null
    private var infiniteAdapter: InfiniteScrollAdapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        shop = get()
        data = shop!!.data
        itemPicker = findViewById(R.id.item_picker)
        itemPicker!!.setOrientation(DSVOrientation.HORIZONTAL)
        itemPicker!!.addOnItemChangedListener(this)
        infiniteAdapter = InfiniteScrollAdapter.wrap(ShopAdapter(data!!))
        itemPicker!!.adapter = infiniteAdapter


        itemPicker!!.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build()
        )

        findViewById<View>(R.id.item_btn_buy).setOnClickListener(this)
//        findViewById<View>(R.id.home).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
R.id.item_btn_buy -> startActivity<ManageStampCouponActivity>()
//            R.id.home -> finish()
//            else ->
        }
    }


    override fun onCurrentItemChanged(viewHolder: ShopAdapter.ViewHolder?, adapterPosition: Int) {
        val positionInDataSet: Int = infiniteAdapter!!.getRealPosition(adapterPosition)
    }


}