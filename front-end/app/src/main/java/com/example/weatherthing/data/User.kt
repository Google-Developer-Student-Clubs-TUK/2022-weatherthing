package com.example.weatherthing.data

data class User(
    val uId: String,
    val email: String,
    val nickname: String,
    val gender: Int,
    val age: Int,
    val weather: Int,
    val regionCode: Int
)
