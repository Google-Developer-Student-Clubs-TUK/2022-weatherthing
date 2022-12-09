package com.example.weatherthing.data

import com.google.firebase.database.DataSnapshot

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

fun fbSnapshotToChatroom(snapshot: DataSnapshot): ChatRoom? {
    snapshot.value?.let { it ->
        val result = it as HashMap<String, Any>?
        val userA = result?.get("userA") as HashMap<String, Any>?
        val userB = result?.get("userB") as HashMap<String, Any>?
        val _chats = result?.get("chats") as ArrayList<HashMap<String, Any>>
        var chats = emptyArray<Chat>()

        if (_chats.isNotEmpty()) {
            _chats.forEach { chat ->
                chats += Chat(
                    from = chat["from"] as String,
                    contents = chat["contents"] as String,
                    createdAt = chat["createdAt"] as String
                )
            }
        }
        return ChatRoom(
            result["id"] as String,
            result["createdAt"] as String,
            ChatUser(userA?.get("id") as String, userA["nickname"] as String),
            ChatUser(userB?.get("id") as String, userA["nickname"] as String),
            chats.toList()
        )
    }
    return null
}
