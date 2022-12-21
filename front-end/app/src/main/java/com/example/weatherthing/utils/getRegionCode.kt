package com.example.weatherthing.utils

import android.location.Geocoder
import android.location.Location
import java.util.*

fun getRegionCode(location: Location): Int {
    val geocoder = Geocoder(App.context, Locale.KOREA)
    val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)

    val region = address[0].getAddressLine(0).split(" ")[0]
    var code = 0
    regionCode.keys.map { s -> if (s.contains(region)) code = regionCode[s] ?: 0 }
    return code
}

val regionCode: Map<String, Int> = mapOf(
    "서울특별시" to 1,
    "경기도" to 2,
    "인천광역시" to 3,
    "강원도" to 4,
    "전라남도" to 5,
    "전라북도" to 6,
    "경상북도" to 7,
    "경상남도" to 8,
    "제주특별자치도" to 9,
    "대구광역시" to 10,
    "세종특별자치도" to 11
)
