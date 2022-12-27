package com.example.weatherthing.scenarios.main.chat

import android.util.Log

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.weatherthing.data.Chat
import com.example.weatherthing.data.ChatRoom
import com.example.weatherthing.data.User

@Composable
fun ChatScreen(navController: NavHostController, chatViewModel: ChatViewModel = viewModel(), chatId: String, postId: Int) {
    var declarationDialogState by remember {
        mutableStateOf(false)
    }
    val user by remember {
        mutableStateOf(chatViewModel.user!!)
    }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontSize = 17.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(painterResource(id = R.drawable.icon_back), contentDescription = "뒤로가기", modifier = Modifier.size(35.dp), tint = colorResource(
//                            id = R.color.green)
//                        )
                    }
                },
                actions = {
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                ChatSection(chatViewModel.chats.collectAsState(), chatViewModel.chatRoom.collectAsState(), user, Modifier.weight(1f), navController)
                SendSection(chatViewModel, chatId, user.id!!)
            }
        }
    }

    LaunchedEffect(Unit) {
        // 채팅창 들어가서 정보 가져오기- 채팅 데이터, 채팅 내용
        chatViewModel.enterChatRoom(chatId)
    }
}

// @OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
// @Composable
// fun TopBarSection(chatViewModel: ChatViewModel, navController: NavHostController, postData: State<PostData?>, userId: Int, alignment: Alignment.Vertical = Alignment.Top) {
//    val chatData = chatViewModel.chatRoom.collectAsState()
// }

@Composable
fun ChatSection(message: State<List<Chat>?>, chatData: State<ChatRoom?>, user: User, modifier: Modifier = Modifier, navController: NavHostController) {
    val otherNickname = if (chatData.value?.userA?.id == user.id) {
        user.nickname
    } else {
        chatData.value?.userB?.nickname ?: ""
    }

    LazyColumn(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 15.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        message.value?.let {
            items(it) { message ->
                ChatItem(message, otherNickname, user.id!!, navController)

                Spacer(modifier = Modifier.height(13.dp))
            }
        }
    }
}

@Composable
fun ChatItem(chat: Chat, nickname: String, id: Int, navController: NavHostController) {
    val current = System.currentTimeMillis()

//    val calculateTime = CalculateTime()
//    val time = calculateTime.calTimeToChat(current, chat.createdAt)

    // 본인일때 true
    val isMe = chat.from == id
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (!isMe) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        if (isMe) {
//            Text(text = time, color = Color.Gray, modifier = Modifier.padding(start = 7.dp, end = 7.dp), fontSize = 13.sp)
        }
        Column {
            if (!isMe) {
                Row(
                    modifier = Modifier.clickable {
                        navController.navigate("")
                    }
                ) {
                    Text(text = nickname, color = Color.Black, fontSize = 13.sp, modifier = Modifier.padding(bottom = 5.dp, end = 5.dp))
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "App icon",
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .size(17.dp)
                    )
                }
            }
            if (chat.contents != "") {
                Box(
                    modifier = if (isMe) {
                        Modifier
                            .background(
                                color = Color.Yellow,
                                shape = RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp)
                            )
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                    } else {
                        Modifier
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(0.dp, 10.dp, 10.dp, 10.dp)
                            )
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                    }
                ) {
                    Text(text = chat.contents, color = Color.Black)
                }
            }
        }
        if (!isMe) {
//            Text(text = time, color = Color.Gray, modifier = Modifier.padding(start = 7.dp), fontSize = 13.sp)
        }
    }
}

@Composable
fun SendSection(viewModel: ChatViewModel, chatId: String, userId: Int) {
    val sendMessage = remember {
        mutableStateOf("")
    }
    val timestamp = remember {
        mutableStateOf<Long>(0)
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = sendMessage.value,
            onValueChange = { sendMessage.value = it },
            placeholder = { Text(text = "메세지를 작성해주세요") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        // 메세지 보내기
                        if (sendMessage.value.isNotEmpty()) {
                            Log.d("새로운 메세지", sendMessage.value)
                            timestamp.value = System.currentTimeMillis()
                            val message = Chat(userId, sendMessage.value, timestamp.value.toString())
                            viewModel.newMessage(chatId, message)
                            sendMessage.value = ""
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "보내기",
                        tint = Color.Black
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.White),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    // 메세지 보내기
                    if (sendMessage.value.isNotEmpty()) {
                        Log.d("새로운 메세지", sendMessage.value)
                        timestamp.value = System.currentTimeMillis()
                        val message =
                            Chat(userId, sendMessage.value, timestamp.value.toString())
                        viewModel.newMessage(chatId, message)
                        sendMessage.value = ""
                    }
                }
            )
        )
    }
}
