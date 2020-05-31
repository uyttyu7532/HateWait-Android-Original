package com.example.hatewait

import android.content.SharedPreferences


fun<T> setShared(pref:SharedPreferences,key:String,data:T){
    val editor = pref.edit()
    if(data is Boolean){
        editor.putBoolean(key, data).apply()
    }
    if(data is String){
        editor.putString(key, data).apply()
    }
}

fun getShared1(pref: SharedPreferences,key:String) : String {
    return pref.getString(key, "null")
}


fun getShared2(pref: SharedPreferences,key:String) : String {
    return pref.getString(key, "null")
}

