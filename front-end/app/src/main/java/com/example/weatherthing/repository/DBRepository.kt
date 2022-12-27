package com.example.weatherthing.repository

import android.util.Log
import com.example.weatherthing.data.User
import com.example.weatherthing.data.UserDBResponse
import com.example.weatherthing.di.NetworkModule
import com.example.weatherthing.network.Load
import io.ktor.client.request.*
import io.ktor.http.*

object DBRepository {
    val BASE_URL: String = "http://10.0.2.2:8080"

    suspend fun getProfile(uId: String): Load =
        kotlin.runCatching {
            NetworkModule.client.get<UserDBResponse?>(BASE_URL + "/api/v1/users") {
                parameter("userId", uId)
            }
        }.getOrNull()?.let {
            Load.Success(
                User(
                    it.id,
                    it.uid,
                    it.email,
                    it.nickname,
                    it.genderCode,
                    it.age,
                    it.weatherCode,
                    it.regionCode
                )
            )
        } ?: Load.Fail("fail")

    suspend fun joinUser(user: User): Load =
        kotlin.runCatching {
            NetworkModule.client.post<UserDBResponse>(BASE_URL + "/api/v1/users") {
                body = user
                contentType(ContentType.Application.Json)
            }
        }.getOrNull()?.let { result ->
            Load.Success(
                result
            )
        } ?: Load.Fail("fail")
}
