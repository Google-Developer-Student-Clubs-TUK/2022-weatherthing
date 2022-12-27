package com.example.weatherthing.repository

import com.example.weatherthing.data.User
import com.example.weatherthing.data.UserDBResponse
import com.example.weatherthing.di.NetworkModule
import com.example.weatherthing.network.Load
import io.ktor.client.request.*
import io.ktor.http.*

object DBRepository {
    val BASE_URL: String = ""

    suspend fun getProfile(uId: String): Load =
        kotlin.runCatching {
            NetworkModule.client.get<UserDBResponse?>(BASE_URL + "/api/v1/users") {
                parameter("userId", uId)
            }
        }.getOrNull()?.let {
            Load.Success(
                User(
                    it.uId,
                    it.email,
                    it.nickname,
                    it.gender,
                    it.age,
                    it.weather,
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
