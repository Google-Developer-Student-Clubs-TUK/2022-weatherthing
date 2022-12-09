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
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
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
        fbGetValue(firebaseDB.reference.child("chat").child(chatId)){
            setListener(it)
        }

        val chatListener = fbChatListener(viewModelScope, firebaseDB.reference.child("chat").child(chatId).child("messages")){updateChats(it)
        }
    }
    private fun setListener(dataSnapshot: DataSnapshot){
        val _chatRoom = fbSnapshotToChatroom(dataSnapshot)

        if (fbSnapshotToChatroom(dataSnapshot) != null) {
            chatRoom.value = _chatRoom
        }
    }

    private fun updateChats(snapshot: DataSnapshot){
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

    fun newMessage(chatId: String, chat: Chat) {
        if (chats.value.isEmpty()) {
            // 첫 메세지일때 채팅방 생성
            chats.value = listOf(chat)
            newChatRoom(chatId, chats.value)
        } else {
            chats.value += chat
            Log.d("추가됨", chats.value.toString())
            fbSetValue(firebaseDB.reference.child("chat").child(chatId).child("messages"), chats.value){}
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

                fbSetValue(firebaseDB.reference.child("chat").child(chatId), chatroomData){enterChatRoom(chatId)}
                Log.d("유저 채팅 리스트1", chatRoomList.value.toString())
                    chatRoomList.value += chatId
                Log.d("유저 채팅 리스트", chatRoomList.value.toString())

                fbSetValue(firebaseDB.reference.child("user").child(user!!.uId), chatRoomList.value){enterChatRoom(chatId)}
            }
        }
    }
    private fun fbGetValue(reference: DatabaseReference, onSuccess: (DataSnapshot) -> Unit){
        reference.get()
            .addOnSuccessListener {
                onSuccess(it)
            }
            .addOnFailureListener {
                Log.d("채팅룸 정보 가져오기 실패", it.toString())
            }
    }

    private fun fbSetValue(reference: DatabaseReference, data: Any, onSuccess: ()-> Unit){
       reference.setValue(data)
            .addOnSuccessListener {
                Log.d("newChatRoomSuccess", "유저 정보에 추가 완료")
                onSuccess()
            }
            .addOnFailureListener {
                Log.d("유저 정보 추가 실패", it.toString())
            }
    }


}
