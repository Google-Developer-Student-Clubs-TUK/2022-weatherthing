package com.example.weatherthing.scenarios.main.chat

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherthing.data.ChatRoom
import com.example.weatherthing.data.fbSnapshotToChatroom
import com.example.weatherthing.utils.App
import com.example.weatherthing.utils.AppPref
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class ChatListViewModel : ViewModel() {
    val chatRooms = MutableStateFlow<List<ChatRoom>>(emptyList())
    private val chatIdList = MutableStateFlow<List<String>>(emptyList())
    val user = AppPref(App.context).getUserPref()!!

    private val firebaseDB = FirebaseDatabase.getInstance()

    init {
        fbChatListener(
            viewModelScope,
            firebaseDB.reference.child("user").child(user.uid)
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
            list.forEach { id ->
                if (!chatIdList.value.contains(id)) {
                    fbChatListener(
                        viewModelScope,
                        firebaseDB.reference.child("chat").child(id)
                    ) { dataSnapshot ->
                        val _chatRoom = fbSnapshotToChatroom(dataSnapshot)
                        chatRooms.value = chatRooms.value.filterNot { chatRoom: ChatRoom -> chatRoom.id == _chatRoom?.id }
                        chatRooms.value.plus(_chatRoom)
                    }
                }
            }
            chatIdList.value = list
        }
    }
}
