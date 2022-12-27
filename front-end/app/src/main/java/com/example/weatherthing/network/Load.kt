package com.example.weatherthing.network

sealed class Load {
    object Loading : Load()
    data class Success<T>(val data: T) : Load()
    data class Fail(val errMsg: String) : Load()
}
