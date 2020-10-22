package com.example.hatewait.discretescroll

import android.content.Context
import android.content.SharedPreferences
import com.example.hatewait.R
import java.util.*

/**
 * Created by yarolegovich on 07.03.2017.
 */
class Shop private constructor() {
//    private val storage: SharedPreferences
    val data: List<Item>
        get() = Arrays.asList(
            Item(1, "Everyday Candle", "$12.00 USD", R.drawable.shop1),
            Item(2, "Small Porcelain Bowl", "$50.00 USD", R.drawable.shop2),
            Item(3, "Favourite Board", "$265.00 USD", R.drawable.shop3),
            Item(4, "Earthenware Bowl", "$18.00 USD", R.drawable.shop4),
            Item(5, "Porcelain Dessert Plate", "$36.00 USD", R.drawable.shop5),
            Item(6, "Detailed Rolling Pin", "$145.00 USD", R.drawable.shop6)
        )

//    fun isRated(itemId: Int): Boolean {
//        return storage.getBoolean(itemId.toString(), false)
//    }

//    fun setRated(itemId: Int, isRated: Boolean) {
//        storage.edit().putBoolean(itemId.toString(), isRated).apply()
//    }

    companion object {
        private const val STORAGE = "shop"
        @JvmStatic
        fun get(): Shop {
            return Shop()
        }
    }

    init {
//        storage = App.getInstance().getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
    }
}