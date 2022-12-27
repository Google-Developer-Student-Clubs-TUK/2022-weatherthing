package com.example.weatherthing.repository

import com.example.weatherthing.BuildConfig
import com.example.weatherthing.data.WeatherData
import com.example.weatherthing.di.NetworkModule
import io.ktor.client.request.*

object WeatherRepository {
    const val API_KEY = BuildConfig.OPENWEATHER_API_KEY
    const val URL = "https://api.openweathermap.org"
    
    suspend fun getWeather(latitude: Double, longitude: Double): WeatherData {
        return NetworkModule.client.get(URL + "/data/2.5/weather") {
            parameter("appid", API_KEY)
            parameter("lat", latitude)
            parameter("lon", longitude)
            parameter("lang", "kr")
        }
    }
}