package com.example.hatewait.model

data class Restaurant(
    val meta: RestaurantMeta,
    val documents: List<RestaurantDocument>
)

data class RestaurantMeta(
    val total_count: Int,  // 검색어에 검색된 문서 수
    val pageable_count: Int, // total_count 중 노출 가능 문서 수, 최대 45
    val is_end: Boolean,    // 현재 페이지가 마지막 페이지인지 여부 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
    val same_name: RestaurantSameName // 질의어의 지역 및 키워드 분석 정보
)

data class RestaurantSameName(
    val region: List<String>, // 	질의어에서 인식된 지역의 리스트
    val keyword: String, // 질의어에서 지역 정보를 제외한 키워드
    val selected_region: String // 인식된 지역 리스트 중, 현재 검색에 사용된 지역 정보

)

data class RestaurantDocument(
    val id: String, // 장소 ID
    val place_name: String, // 장소명, 업체명
    val category_name: String, // 카테고리 이름
    val category_group_code: String, // 중요 카테고리만 그룹핑한 카테고리 그룹 코드
    val category_group_name: String, // 중요 카테고리만 그룹핑한 카테고리 그룹명
    val phone: String, // 전화번호
    val address_name: String, // 전체 지번 주소
    val road_address_name: String, // 전체 도로명 주소
    val x: Double, // X 좌표값 혹은 longitude
    val y: Double, // Y 좌표값 혹은 latitude
    val place_url: String, // 	장소 상세페이지 URL
    val distance: String // 중심좌표까지의 거리 (단, x,y 파라미터를 준 경우에만 존재)단위 meter
)

data class HaitWaitRestaurantRequestData(
    val stores: List<HaitWaitRestaurant>
)

// 회원
data class HaitWaitRestaurant(
    val store_name: String,
    val phone_num: String,
    val address: String,
    val business_time: String,
    val description: String,
    val waiting_num:Int
)