package com.example.weatherthing.di

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

object DataModule {
    const val API_KEY = "9a24b96b3ff42648de6d674032a49494"
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