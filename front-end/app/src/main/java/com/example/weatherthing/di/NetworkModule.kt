package com.example.weatherthing.di

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import com.example.weatherthing.BuildConfig

object NetworkModule {
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