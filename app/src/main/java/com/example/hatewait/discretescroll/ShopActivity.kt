package com.example.hatewait.discretescroll

import android.accessibilityservice.GestureDescription
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hatewait.R
import com.example.hatewait.discretescroll.Shop.Companion.get
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.Pivot

import com.yarolegovich.discretescrollview.transform.ScaleTransformer





class ShopActivity : AppCompatActivity(),
    DiscreteScrollView.OnItemChangedListener<ShopAdapter.ViewHolder?>, View.OnClickListener {
    private var data: List<Item>? = null
    private var shop: Shop? = null
    private var currentItemName: TextView? = null
    private var currentItemPrice: TextView? = null
    private var rateItemButton: ImageView? = null
    private var itemPicker: DiscreteScrollView? = null
    private var infiniteAdapter: InfiniteScrollAdapter<*>? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        currentItemName = findViewById(R.id.item_name)
        currentItemPrice = findViewById(R.id.item_price)
        rateItemButton = findViewById(R.id.item_btn_rate)
        shop = get()
        data = shop!!.data
        itemPicker = findViewById(R.id.item_picker)
        itemPicker!!.setOrientation(DSVOrientation.HORIZONTAL)
        itemPicker!!.addOnItemChangedListener(this)
        infiniteAdapter = InfiniteScrollAdapter.wrap(ShopAdapter(data!!))
        itemPicker!!.adapter = infiniteAdapter
//        itemPicker!!.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime())
//        itemPicker!!.setItemTransformer(
//            GestureDescription.Builder()
//                .setMinScale(0.8f)
//                .build()
//        )

        itemPicker!!.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build()
        )
        onItemChanged(data!![0])
        findViewById<View>(R.id.item_btn_rate).setOnClickListener(this)
        findViewById<View>(R.id.item_btn_buy).setOnClickListener(this)
        findViewById<View>(R.id.item_btn_comment).setOnClickListener(this)
        findViewById<View>(R.id.home).setOnClickListener(this)
        findViewById<View>(R.id.btn_smooth_scroll).setOnClickListener(this)
        findViewById<View>(R.id.btn_transition_time).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.item_btn_rate -> {
                val realPosition: Int =
                    infiniteAdapter!!.getRealPosition(itemPicker!!.getCurrentItem())
                val current = data!![realPosition]
//                shop!!.setRated(current.id, !shop!!.isRated(current.id))
//                changeRateButtonState(current)
            }
            R.id.home -> finish()
            R.id.btn_transition_time -> DiscreteScrollViewOptions.configureTransitionTime(itemPicker)
            R.id.btn_smooth_scroll -> DiscreteScrollViewOptions.smoothScrollToUserSelectedPosition(
                itemPicker,
                v
            )
            else -> showUnsupportedSnackBar()
        }
    }

    private fun onItemChanged(item: Item) {
        currentItemName!!.text = item.name
        currentItemPrice!!.text = item.price
//        changeRateButtonState(item)
    }

//    private fun changeRateButtonState(item: Item) {
//        if (shop!!.isRated(item.id)) {
//            rateItemButton!!.setImageResource(R.drawable.ic_star_black_24dp)
//            rateItemButton!!.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent))
//        } else {
//            rateItemButton!!.setImageResource(R.drawable.ic_star_border_black_24dp)
//            rateItemButton!!.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent))
//        }
//    }

    override fun onCurrentItemChanged(viewHolder: ShopAdapter.ViewHolder?, adapterPosition: Int) {
        val positionInDataSet: Int = infiniteAdapter!!.getRealPosition(adapterPosition)
        onItemChanged(data!![positionInDataSet])
    }

    private fun showUnsupportedSnackBar() {
        Toast.makeText(this, "else", Toast.LENGTH_LONG).show()
//        Snackbar.make(itemPicker, R.string.msg_unsupported_op, Snackbar.LENGTH_SHORT).show()
    }
}