package com.example.weatherthing.di

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import com.example.weatherthing.BuildConfig

object DataModule {
    const val API_KEY = BuildConfig.OPENWEATHER_API_KEY
    const val URL = "https://api.openweathermap.org"

    val client = HttpClient() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }
}