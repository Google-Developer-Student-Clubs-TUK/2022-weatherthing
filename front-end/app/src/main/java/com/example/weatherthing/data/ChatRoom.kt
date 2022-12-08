package com.example.weatherthing.data

data class ChatRoom(
    val id: String,
    val createdAt: String,
    val userA: ChatUser,
    val userB: ChatUser,
    val chats: List<Chat>
)

data class ChatUser(
    val id: String,
    var nickname: String
)