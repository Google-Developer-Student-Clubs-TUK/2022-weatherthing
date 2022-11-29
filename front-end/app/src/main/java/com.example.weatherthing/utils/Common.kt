package com.example.weatherthing.utils

import com.example.weatherthing.R

val imageMap: MutableMap<String, Int> = mapOf<String, Int>(
    "01d" to R.drawable.i01d,
    "01n" to R.drawable.i01n,
    "02d" to R.drawable.i02d,
    "02n" to R.drawable.i02n,
    "03d" to R.drawable.i03d,
    "03n" to R.drawable.i03n,
    "04d" to R.drawable.i04d,
    "04n" to R.drawable.i04n,
    "09d" to R.drawable.i09d,
    "09n" to R.drawable.i09d,
    "10d" to R.drawable.i09d,
    "10n" to R.drawable.i09d,
    "11d" to R.drawable.i11d,
    "11n" to R.drawable.i11d,
    "13d" to R.drawable.i13n,
    "13n" to R.drawable.i13n,
    "50d" to R.drawable.i50n,
    "50n" to R.drawable.i50n
) as MutableMap<String, Int>

fun getWeatherImage(key: String): Int {
    return imageMap[key]!!
}