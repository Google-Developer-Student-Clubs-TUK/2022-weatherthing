package com.example.weatherthing.scenarios.main.chat

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherthing.data.Chat
import com.example.weatherthing.data.ChatRoom
import com.example.weatherthing.data.ChatUser
import com.example.weatherthing.utils.App
import com.example.weatherthing.utils.AppPref
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class ChatListViewModel : ViewModel() {
    val chatRooms = MutableStateFlow<List<ChatRoom>>(emptyList())
    val user = AppPref(App.context).getUserPref()!!

    private val firebaseDB = FirebaseDatabase.getInstance()

    init {
        fbChatListener(
            viewModelScope,
            firebaseDB.reference.child("user").child(user.uId)
        ) {
            getChatData(it)
        }
    }

    private fun fbChatListener(
        scope: CoroutineScope,
        reference: DatabaseReference,
        process: (DataSnapshot) -> Unit
    ) = callbackFlow<Unit> {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                process(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "loadMessage:onCancelled", error.toException())
            }
        }

        reference.addValueEventListener(listener)

        awaitClose {
            reference.removeEventListener(listener)
        }
    }.shareIn(scope, SharingStarted.Eagerly)

    private fun getChatData(snapshot: DataSnapshot) {
        snapshot.value?.let {
            val list = it as ArrayList<String>
            list.forEach { e ->
                fbChatListener(
                    viewModelScope,
                    firebaseDB.reference.child("chat").child(e)
                ) { dataSnapshot ->
                    toChatData(dataSnapshot)
                }
            }
        }
    }

    private fun toChatData(snapshot: DataSnapshot) {
        snapshot.value?.let { it ->
            val result = it as HashMap<String, Any>?
            val userA = result?.get("userA") as HashMap<String, Any>?
            val userB = result?.get("userB") as HashMap<String, Any>?
            val _chats = result?.get("chats") as ArrayList<HashMap<String, Any>>
            var chats: ArrayList<Chat>? = null

            if (!_chats.isNullOrEmpty()) {
                _chats.forEach { chat ->
                    if (chats.isNullOrEmpty()) {
                        chats = arrayListOf(
                            Chat(
                                from = chat["from"] as String,
                                contents = chat["contents"] as String,
                                createdAt = chat["createdAt"] as String
                            )
                        )
                    } else {
                        chats!!.add(
                            Chat(
                                from = chat["from"] as String,
                                contents = chat["contents"] as String,
                                createdAt = chat["createdAt"] as String
                            )
                        )
                    }
                }
            }
            val _chatroom = ChatRoom(
                result?.get("id") as String,
                result.get("createdAt") as String,
                ChatUser(userA?.get("id") as String, userA["nickname"] as String),
                ChatUser(userB?.get("id") as String, userA["nickname"] as String),
                chats!!.toList()
            )

            if (chatRooms.value.isEmpty()) {
                chatRooms.value = listOf(_chatroom)
            } else {
                chatRooms.value += _chatroom
            }
        }
    }
}
