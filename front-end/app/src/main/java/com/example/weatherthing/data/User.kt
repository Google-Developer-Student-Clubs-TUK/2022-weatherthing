package com.example.weatherthing.data

@kotlinx.serialization.Serializable
data class User(
    val id: Int?,
    val uid: String,
    val email: String,
    val nickname: String,
    val genderCode: Int,
    val age: Int,
    val weatherCode: Int,
    val regionCode: Int
)

data class UserDBResponse(
    val createdAt: String?,
    val updateAt: String?,
    val id: Int?,
    val uid: String,
    val email: String,
    val nickname: String,
    val genderCode: Int,
    val age: Int,
    val weatherCode: Int,
    val regionCode: Int
)
