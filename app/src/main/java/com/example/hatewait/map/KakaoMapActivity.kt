package com.example.hatewait.map

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hatewait.R
import com.example.hatewait.model.Restaurant
import kotlinx.android.synthetic.main.activity_kakao_map.*
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPOIItem.CalloutBalloonButtonType
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.CurrentLocationEventListener
import net.daum.mf.map.api.MapView.POIItemEventListener
import org.jetbrains.anko.locationManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private lateinit var mcontext: Context

class KakaoMapActivity : AppCompatActivity(), CurrentLocationEventListener,
    MapView.MapViewEventListener {
    private var mapView: MapView? = null
    private var mapViewContainer: ViewGroup? = null
    private var REQUIRED_PERMISSIONS =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private var mapPointGeo: MapPoint.GeoCoordinate? = null
    lateinit var location: Location
    private var bottom: String? = null
    private var left: String? = null
    private var top: String? = null
    private var right: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_map)

        mcontext = this.applicationContext

        mapView = MapView(this)
        mapViewContainer =
            findViewById<View>(R.id.kakaoMapView) as ViewGroup
        mapViewContainer!!.addView(mapView)
        mapView!!.setMapViewEventListener(this)
//        mapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
        mapView!!.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter())
        mapView!!.setPOIItemEventListener(poiItemEventListener);



        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            checkRunTimePermission()
        }

        // 현재 위치로 이동하게 해야함
        myLocationFAB.setOnClickListener {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            mapView!!.setMapCenterPointAndZoomLevel(
                MapPoint.mapPointWithGeoCoord(
                    location.latitude,
                    location.longitude
                ), 1, true
            );
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        mapViewContainer!!.removeAllViews()
    }

    override fun onCurrentLocationUpdate(
        mapView: MapView,
        currentLocation: MapPoint,
        accuracyInMeters: Float
    ) {

        mapPointGeo = currentLocation.mapPointGeoCoord
        Log.i(
            LOG_TAG,
            String.format(
                "MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)",
                mapPointGeo!!.latitude,
                mapPointGeo!!.longitude,
                accuracyInMeters
            )
        )
    }

    override fun onCurrentLocationDeviceHeadingUpdate(
        mapView: MapView,
        v: Float
    ) {
    }

    override fun onCurrentLocationUpdateFailed(mapView: MapView) {}
    override fun onCurrentLocationUpdateCancelled(mapView: MapView) {}
    private fun onFinishReverseGeoCoding(result: String) {
//        Toast.makeText(LocationDemoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }

    // ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
    override fun onRequestPermissionsResult(
        permsRequestCode: Int,
        permissions: Array<String>,
        grandResults: IntArray
    ) {
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.size == REQUIRED_PERMISSIONS.size) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var check_result = true

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {
                Log.d(LOG_TAG, "start")
                //위치 값을 가져올 수 있음
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있다
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    )
                ) {
                    Toast.makeText(
                        this@KakaoMapActivity,
                        "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@KakaoMapActivity,
                        "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission =
            ContextCompat.checkSelfPermission(
                this@KakaoMapActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@KakaoMapActivity,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(
                    this@KakaoMapActivity,
                    "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                    Toast.LENGTH_LONG
                ).show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@KakaoMapActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@KakaoMapActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting() {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(this@KakaoMapActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            """
                앱을 사용하기 위해서는 위치 서비스가 필요합니다.
                위치 설정을 수정하시겠습니까?
                """.trimIndent()
        )
        builder.setCancelable(true)
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
            val callGPSSettingIntent =
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(
                callGPSSettingIntent,
                GPS_ENABLE_REQUEST_CODE
            )
        })
        builder.setNegativeButton(
            "취소",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        builder.create().show()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->                 //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(LOG_TAG, "onActivityResult : GPS 활성화 되있음")
                        checkRunTimePermission()
                        return
                    }
                }
        }
    }

    fun checkLocationServicesStatus(): Boolean {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }


    override fun onMapViewInitialized(mapView: MapView) {
    }

    override fun onMapViewCenterPointMoved(
        mapView: MapView,
        mapPoint: MapPoint
    ) {
    }

    override fun onMapViewZoomLevelChanged(
        mapView: MapView,
        i: Int
    ) {
    }

    override fun onMapViewSingleTapped(
        mapView: MapView,
        mapPoint: MapPoint
    ) {
    }

    override fun onMapViewDoubleTapped(
        mapView: MapView,
        mapPoint: MapPoint
    ) {
    }

    override fun onMapViewLongPressed(
        mapView: MapView,
        mapPoint: MapPoint
    ) {
    }

    override fun onMapViewDragStarted(
        mapView: MapView,
        mapPoint: MapPoint
    ) {
    }

    override fun onMapViewDragEnded(
        mapView: MapView,
        mapPoint: MapPoint
    ) {

        left = mapView.mapPointBounds.bottomLeft.mapPointGeoCoord.latitude.toString()
        bottom = mapView.mapPointBounds.bottomLeft.mapPointGeoCoord.longitude.toString()
        right = mapView.mapPointBounds.topRight.mapPointGeoCoord.latitude.toString()
        top = mapView.mapPointBounds.topRight.mapPointGeoCoord.longitude.toString()


        SearchRetrofit.getService().requestSearchRestaurant(
            rect = "${bottom},${left},${top},${right}"
        ).enqueue(object :
            Callback<Restaurant> {
            override fun onFailure(call: Call<Restaurant>, t: Throwable) {
            }

            override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                if (response.body() != null) {
                    Toast.makeText(this@KakaoMapActivity, "성공", Toast.LENGTH_SHORT)
                    val restaurants = response.body()!!.documents
                    Log.i(LOG_TAG, restaurants.toString())
                    val restaurantsIterator = restaurants.listIterator()

                    mapView!!.removeAllPOIItems()

                    while (restaurantsIterator.hasNext()) {
                        val restaurant = restaurantsIterator.next()

                        val marker = MapPOIItem()

                        //맵 포인트 위도경도 설정
                        val mapPoint = MapPoint.mapPointWithGeoCoord(restaurant.y, restaurant.x)
                        marker.itemName = restaurant.place_name
                        marker.userObject =
                            restaurant.category_name + "," + restaurant.phone + "," + restaurant.place_url
                        marker.mapPoint = mapPoint
                        marker.markerType =
                            MapPOIItem.MarkerType.CustomImage // 기본으로 제공하는 BluePin 마커 모양.
                        marker.selectedMarkerType = MapPOIItem.MarkerType.CustomImage
                        marker.customImageResourceId =
                            R.drawable.pin
//                        marker.isCustomImageAutoscale = false; // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                        marker.setCustomImageAnchor(
                            0.5f,
                            1.0f
                        ); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
                        mapView!!.addPOIItem(marker)

                    }
                }
            }

        })
    }

    override fun onMapViewMoveFinished(
        mapView: MapView,
        mapPoint: MapPoint
    ) {

    }

    private val poiItemEventListener: POIItemEventListener = object : POIItemEventListener { // 클릭 이벤트
        override fun onPOIItemSelected(
            mapView: MapView,
            mapPOIItem: MapPOIItem
        ) { // // 마커 클릭 후 info window 띄워졌을 때
            //sample code 없음
//            Log.i("LOG_TAG", "onPOIItemSelected")
        }

        override fun onCalloutBalloonOfPOIItemTouched(
            mapView: MapView,
            mapPOIItem: MapPOIItem
        ) { // info window 클릭했을 때

//            Log.i("LOG_TAG", "onCalloutBalloonOfPOIItemTouched")
//            Log.i("LOG_TAG", mapPOIItem.userObject.toString().split(",")[1])
//            Log.i("LOG_TAG", mapPOIItem.userObject.toString().split(",")[2])


//            try {
//                var intent = Intent(
//                    Intent.ACTION_DIAL,
//                    Uri.parse("tel:"+mapPOIItem.userObject.toString().split(",")[1])
//                )
//                intent.flags = FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
//            }
//            catch(e: ActivityNotFoundException){
//                Log.i(LOG_TAG, e.toString());
//            }

            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapPOIItem.userObject.toString().split(",")[2]))
            intent.flags= FLAG_ACTIVITY_NEW_TASK
            mcontext.startActivity(intent)
        }

        override fun onCalloutBalloonOfPOIItemTouched(
            mapView: MapView,
            mapPOIItem: MapPOIItem,
            calloutBalloonButtonType: CalloutBalloonButtonType
        ) {
            //sample code 없음
//            Log.i("LOG_TAG", "onCalloutBalloonOfPOIItemTouched")
        }

        override fun onDraggablePOIItemMoved(
            mapView: MapView,
            mapPOIItem: MapPOIItem,
            mapPoint: MapPoint
        ) {
            //sample code 없음
//            Log.i("LOG_TAG", "onDraggablePOIItemMoved")
        }
    }


    internal class CustomCalloutBalloonAdapter : CalloutBalloonAdapter {


        private val mCalloutBalloon: View =
            LayoutInflater.from(mcontext).inflate(
                R.layout.custom_callout_balloon, null
            )


//        val balloonPhonenum = mCalloutBalloon.findViewById(R.id.balloon_phonenum) as TextView
//
//        // 클릭이벤트 왜 안돼
//        init {
//            mCalloutBalloon.setOnClickListener {
//                Log.i("click", "클릭이벤트")
//                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(balloonPhonenum.text.toString()))
//                intent.flags = FLAG_ACTIVITY_NEW_TASK
//                mcontext.startActivity(intent)
//            }
//
//        }


        override fun getCalloutBalloon(poiItem: MapPOIItem): View {
            (mCalloutBalloon.findViewById(R.id.balloon_category) as TextView).text =
                poiItem.userObject.toString().split(",")[0]
            (mCalloutBalloon.findViewById(R.id.balloon_title) as TextView).text = poiItem.itemName
//            (mCalloutBalloon.findViewById(R.id.balloon_phonenum) as TextView).text =
//                poiItem.userObject.toString().split(",")[1]
//            (mCalloutBalloon.findViewById(R.id.balloon_url) as TextView).text =
//                poiItem.userObject.toString().split(",")[2]


            return mCalloutBalloon
        }

        override fun getPressedCalloutBalloon(poiItem: MapPOIItem): View? {

            return mCalloutBalloon
        }

    }


    companion object {
        private const val LOG_TAG = "KakaoMapActivity"
        private const val GPS_ENABLE_REQUEST_CODE = 2001
        private const val PERMISSIONS_REQUEST_CODE = 100
    }
}