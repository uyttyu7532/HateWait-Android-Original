package com.example.hatewait.discretescroll

import com.example.hatewait.R
import java.util.*


class Shop private constructor() {
    val data: List<Coupon>
//        get() = listOf(
//            Item(1, "Everyday Candle", "$12.00 USD", R.drawable.shop1),
//            Item(2, "Small Porcelain Bowl", "$50.00 USD", R.drawable.shop2),
//            Item(3, "Favourite Board", "$265.00 USD", R.drawable.shop3),
//            Item(4, "Earthenware Bowl", "$18.00 USD", R.drawable.shop4),
//            Item(5, "Porcelain Dessert Plate", "$36.00 USD", R.drawable.shop5),
//            Item(6, "Detailed Rolling Pin", "$145.00 USD", R.drawable.shop6)
//        )

//data class Coupon(val id: Int, val benefit: String, val remark: String,  val issue_date: String, val expiration_date:String, val used_date:String)
        get() = listOf(
    Coupon(1, "자몽에이드", "메인 메뉴 주문시 사용가능", "2020-06-15 15:18","2020-10-15 15:18","2020-09-15 15:18"),
    Coupon(2, "샐러드", "메인 메뉴 주문시 사용가능", "2020-06-14 15:18","2020-10-14 15:18","2020-09-15 15:18"),
    Coupon(3, "자몽에이드", "평일에만 사용 가능", "2020-06-15 18:18","2020-10-15 18:18","2020-09-15 15:18"),
    Coupon(4, "자몽에이드", "", "2020-06-15 15:18","2021-06-15 15:18","2020-09-15 15:18"),
    Coupon(5, "탄산음료 1잔", "메인 메뉴 주문시 사용가능", "2020-06-15 15:18","2020-10-15 15:18","2020-09-15 15:18"),
    Coupon(6, "자몽에이드", "메인 메뉴 주문시 사용가능", "2020-06-15 15:18","2020-10-15 15:18","2020-09-15 15:18")
        )



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