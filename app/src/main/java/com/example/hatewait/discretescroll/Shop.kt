package com.example.hatewait.discretescroll

import com.example.hatewait.R
import java.util.*


class Shop private constructor() {
    val data: List<Item>
        get() = listOf(
            Item(1, "Everyday Candle", "$12.00 USD", R.drawable.shop1),
            Item(2, "Small Porcelain Bowl", "$50.00 USD", R.drawable.shop2),
            Item(3, "Favourite Board", "$265.00 USD", R.drawable.shop3),
            Item(4, "Earthenware Bowl", "$18.00 USD", R.drawable.shop4),
            Item(5, "Porcelain Dessert Plate", "$36.00 USD", R.drawable.shop5),
            Item(6, "Detailed Rolling Pin", "$145.00 USD", R.drawable.shop6)
        )

//        get() = listOf(
//            Item(1, "Everyday Candle", "$12.00 USD"),
//            Item(2, "Small Porcelain Bowl", "$50.00 USD"),
//            Item(3, "Favourite Board", "$265.00 USD"),
//            Item(4, "Earthenware Bowl", "$18.00 USD"),
//            Item(5, "Porcelain Dessert Plate", "$36.00 USD"),
//            Item(6, "Detailed Rolling Pin", "$145.00 USD")
//        )



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