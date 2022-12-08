package com.example.weatherthing.scenarios.main.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.weatherthing.R
import com.example.weatherthing.data.ChatRoom

@Composable
fun ChatListScreen(navController: NavHostController) {
    val chatListViewModel by remember {
        mutableStateOf(ChatListViewModel())
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "채팅목록",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 50.dp),
                        fontSize = 17.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기",
                            modifier = Modifier.size(35.dp),
                            tint = Color.Black
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            val chatList = chatListViewModel.chatRooms.collectAsState()
            LazyColumn() {
                items(chatList.value) { chat ->
                    ChatCard(
                        chat = chat,
                        navController = navController,
                        viewModel = chatListViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatCard(chat: ChatRoom, navController: NavController, viewModel: ChatListViewModel) {
    // chat 내용이 바뀔 때마다 chatcard가 업데이트 되게
    val nickname =
        if (chat.userA.nickname == viewModel.user.nickname) viewModel.user.nickname else chat.userB.nickname

    Card(
        onClick = { navController.navigate(route = "") },
        modifier = Modifier.fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                top = 15.dp,
                bottom = 15.dp
            )
        ) {
            val (text, time) = createRefs()
            Column(
                modifier = Modifier.constrainAs(text) {
                    start.linkTo(parent.start)
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text =nickname,
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "App icon",
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .size(22.dp)
                            .padding(start = 5.dp)
                    )
                }
                Text(
                    text = chat.chats.last().contents,
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 7.dp, start = 7.dp)
                )

            }
//            lastMessage?.let {
//                val current = System.currentTimeMillis()
//
//                val calculateTime = CalculateTime()
//                val timeText = calculateTime.calTimeToChat(current, lastMessage.createdAt)
//                Text(
//                    text = timeText,
//                    fontSize = 12.sp,
//                    color = Color.DarkGray,
//                    modifier = Modifier.constrainAs(time) {
//                        end.linkTo(parent.end)
//                        top.linkTo(parent.top)
//                    }
//                )
//            }
        }
    }
}
