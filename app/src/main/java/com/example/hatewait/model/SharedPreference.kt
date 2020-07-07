package com.example.hatewait.model

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

fun getShared(pref: SharedPreferences,key:String) : Boolean{
    return pref.getBoolean(key, false)
}



