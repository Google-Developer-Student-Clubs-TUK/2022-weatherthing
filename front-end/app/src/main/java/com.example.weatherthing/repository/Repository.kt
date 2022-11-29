package com.example.weatherthing.repository

import com.example.weatherthing.data.WeatherData
import com.example.weatherthing.di.DataModule
import com.example.weatherthing.di.DataModule.API_KEY
import com.example.weatherthing.di.DataModule.URL
import io.ktor.client.request.*

object Repository {
    suspend fun getWeather(latitude: Double, longitude: Double): WeatherData {
        return DataModule.client.get(URL + "/data/2.5/weather") {
            parameter("appid", API_KEY)
            parameter("lat", latitude)
            parameter("lon", longitude)
            parameter("lang", "kr")
        }
    }
}