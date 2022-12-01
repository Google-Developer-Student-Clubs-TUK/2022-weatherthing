package com.example.weatherthing.utils

val imageMap: MutableMap<String, String> = mapOf(
    "01d" to "https://assets7.lottiefiles.com/packages/lf20_5i5k8eh3.json",
    "01n" to "https://assets8.lottiefiles.com/private_files/lf30_dbkiaaqd.json",
    "02d" to "https://assets2.lottiefiles.com/private_files/lf30_yh3ay76n.json",
    "02n" to "https://assets2.lottiefiles.com/private_files/lf30_yh3ay76n.json",
    "03d" to "https://assets2.lottiefiles.com/private_files/lf30_yh3ay76n.json",
    "03n" to "https://assets2.lottiefiles.com/private_files/lf30_yh3ay76n.json",
    "04d" to "https://assets2.lottiefiles.com/private_files/lf30_yh3ay76n.json",
    "04n" to "https://assets2.lottiefiles.com/private_files/lf30_yh3ay76n.json",
    "09d" to "https://assets8.lottiefiles.com/packages/lf20_h0cc4ii6.json",
    "09n" to "https://assets8.lottiefiles.com/packages/lf20_h0cc4ii6.json",
    "10d" to "https://assets8.lottiefiles.com/packages/lf20_h0cc4ii6.json",
    "10n" to "https://assets8.lottiefiles.com/packages/lf20_h0cc4ii6.json",
    "11d" to "https://assets7.lottiefiles.com/temp/lf20_Kuot2e.json",
    "11n" to "https://assets7.lottiefiles.com/temp/lf20_Kuot2e.json",
    "13d" to "https://assets10.lottiefiles.com/packages/lf20_N0y2Nj.json",
    "13n" to "https://assets10.lottiefiles.com/packages/lf20_N0y2Nj.json",
    "50d" to "https://assets4.lottiefiles.com/temp/lf20_kOfPKE.json",
    "50n" to "https://assets4.lottiefiles.com/temp/lf20_kOfPKE.json"
) as MutableMap<String, String>

fun getWeatherAnimationUrl(key: String?): String {
    return imageMap[key] ?: "https://assets7.lottiefiles.com/packages/lf20_5i5k8eh3.json"
}
