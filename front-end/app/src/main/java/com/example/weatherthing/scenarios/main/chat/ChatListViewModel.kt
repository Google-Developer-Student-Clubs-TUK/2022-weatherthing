package com.example.weatherthing.scenarios.main.chat

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherthing.data.Chat
import com.example.weatherthing.data.ChatRoom
import com.example.weatherthing.data.ChatUser
import com.example.weatherthing.data.fbSnapshotToChatroom
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
        val _chatRooms = emptyList<ChatRoom>()
        snapshot.value?.let {
            val list = it as ArrayList<String>
            list.forEach { e ->
                fbChatListener(
                    viewModelScope,
                    firebaseDB.reference.child("chat").child(e)
                ) { dataSnapshot ->
                    var isExist = false
                    val _chatRoom = fbSnapshotToChatroom(dataSnapshot)
                    for (chatroom in chatRooms.value) {
                        if (chatroom.id == _chatRoom?.id) {
                            val index = chatRooms.value.indexOf(chatroom)
                            chatRooms.value = chatRooms.value.drop(index)
                            chatRooms.value.plus(_chatRoom)
                            isExist = true
                        }
                    }
                    if (!isExist) {
                        _chatRooms.plus(_chatRoom)
                    }
                }
            }
        }
        chatRooms.value = _chatRooms
    }
}
