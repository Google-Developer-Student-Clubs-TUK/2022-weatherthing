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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatViewModel : ViewModel() {

    private val firebaseDB = FirebaseDatabase.getInstance()
    var chats = MutableStateFlow<List<Chat>>(emptyList())
    var chatRoom = MutableStateFlow<ChatRoom?>(null)
    private var chatRoomList = MutableStateFlow<List<String>>(emptyList())
    var user = AppPref(App.context).getUserPref()

    init {
        firebaseDB.reference.child("user").child(user!!.uId).get()
            .addOnSuccessListener {
                it.value?.let { it ->
                    chatRoomList.value = it as List<String>
                }
                Log.d("유저 채팅 정보 가져옴", chatRoomList.value.toString())
            }
            .addOnFailureListener {
            }
    }

    // 채팅방 들어감
    fun enterChatRoom(chatId: String) {
        // 한번도 채팅하지 않은경우는 조회 불가
        Log.d("채팅방 id", chatId)
        firebaseDB.reference.child("chat").child(chatId).get()
            .addOnSuccessListener {
                Log.d("채팅방 정보", it.value.toString())
                it.value?.let { value ->
                    val result = value as HashMap<String, Any>?
                    val userA = result?.get("userA") as HashMap<String, Any>?
                    val userB = result?.get("userB") as HashMap<String, Any>?
                    val _chatData = ChatRoom(
                        result?.get("id") as String,
                        result.get("createdAt") as String,
                        ChatUser(userA?.get("id") as String, userA["nickname"] as String),
                        ChatUser(userB?.get("id") as String, userA["nickname"] as String),
                        result["chats"] as List<Chat>
                    )
                    chatRoom.value = _chatData
                }
            }
            .addOnFailureListener {
                Log.d("채팅룸 정보 가져오기 실패", it.toString())
            }

        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val _chats = arrayListOf<Chat>()
                val data = snapshot.value as ArrayList<HashMap<String, Any>>?

                Log.d("변화 리스너", snapshot.value.toString())
                data?.forEach {
                    _chats.add(
                        Chat(
                            it["contents"] as String,
                            it["createdAt"] as String,
                            it["from"] as String
                        )
                    )
                }
                chats.value = arrayListOf()
                chats.value = _chats.toList()
                Log.d("변화 리스너2", chats.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "loadMessage:onCancelled", error.toException())
            }
        }
        firebaseDB.reference.child("chat").child(chatId).child("messages")
            .addValueEventListener(chatListener)
    }

    fun newMessage(chatId: String, chat: Chat) {
        if (chats.value.isEmpty()) {
            // 첫 메세지일때 채팅방 생성
            chats.value = listOf(chat)
            newChatRoom(chatId, chats.value)
        } else {
            chats.value += chat
            Log.d("추가됨", chats.value.toString())
            firebaseDB.reference.child("chat").child(chatId).child("messages").setValue(chats.value)
                .addOnSuccessListener {
                    Log.d("newChatRoomSuccess", "메세지 보내기 성공")
                }
                .addOnFailureListener {
                    Log.d("메세지 보내기 실패", it.toString())
                }
        }
    }

    private fun newChatRoom(chatId: String, chat: List<Chat>) {
        val timeStamp: String = SimpleDateFormat(
            "yyyy-MM-dd HH:MM:ss",
            Locale.KOREA
        ).format(Date(System.currentTimeMillis()))
        viewModelScope.launch {
            if (chatId !in chatRoomList.value) {
                val notMe = ChatUser("", "")
                val me = ChatUser(user!!.uId, user!!.nickname)
                val chatroomData = ChatRoom(chatId, timeStamp, notMe, me, chat)

                firebaseDB.reference.child("chat").child(chatId).setValue(chatroomData)
                    .addOnSuccessListener {
                        Log.d("newChatRoomSuccess", "채팅룸 생성 완료")
                        enterChatRoom(chatId)
                    }
                    .addOnFailureListener {
                        Log.d("채팅룸 생성 실패", it.toString())
                    }

                Log.d("유저 채팅 리스트1", chatRoomList.value.toString())
                if (chatRoomList.value.isEmpty()) {
                    chatRoomList.value = listOf(chatId)
                    Log.d("리스트 빔", chatRoomList.value.toString())
                } else {
                    chatRoomList.value += chatId
                    Log.d("리스트 안빔", chatRoomList.value.toString())
                }
                Log.d("유저 채팅 리스트", chatRoomList.value.toString())

                firebaseDB.reference.child("user").child(user!!.uId).setValue(chatRoomList.value)
                    .addOnSuccessListener {
                        Log.d("newChatRoomSuccess", "유저 정보에 추가 완료")
                        enterChatRoom(chatId)
                    }
                    .addOnFailureListener {
                        Log.d("유저 정보 추가 실패", it.toString())
                    }
            }
        }
    }
}
