package com.example.weatherthing.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CommentData(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("image")
    val image: Int,
    @SerializedName("content")
    val comment: String
): Serializable

data class CommentDBResponse(
    val id: Int?,
    val userId: Int,
    val content: String
)