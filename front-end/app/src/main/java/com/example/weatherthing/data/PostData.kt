package com.example.weatherthing.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class PostData(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("image")
    val image: Int,
    @SerializedName("content")
    val content: String
): Serializable

data class PostDBResponse(
    val id: Int?,
    val userId: Int,
    val content: String
)