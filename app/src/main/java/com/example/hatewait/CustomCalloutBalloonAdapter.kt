//package com.example.hatewait
//
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import net.daum.mf.map.api.CalloutBalloonAdapter
//import net.daum.mf.map.api.MapPOIItem
//
//
//internal class CustomCalloutBalloonAdapter : CalloutBalloonAdapter {
//	private val mCalloutBalloon: View
//	override fun getCalloutBalloon(poiItem: MapPOIItem): View {
//		(mCalloutBalloon.findViewById(R.id.badge) as ImageView).setImageResource(R.drawable.ic_launcher)
//		(mCalloutBalloon.findViewById(R.id.title) as TextView).text = poiItem.itemName
//		(mCalloutBalloon.findViewById(R.id.desc) as TextView).text("Custom CalloutBalloon")
//		return mCalloutBalloon
//	}
//
//	override fun getPressedCalloutBalloon(poiItem: MapPOIItem): View {
//		return null
//	}
//
//	init {
//		mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null)
//	}
//}