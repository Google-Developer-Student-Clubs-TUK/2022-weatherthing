package com.example.weatherthing.scenarios.main.Post

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Message
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.weatherthing.R
import com.example.weatherthing.scenarios.main.NavItem
import com.example.weatherthing.ui.theme.skyblue

@Composable
fun CommentScreen(navController: NavHostController) {
    var value by remember { mutableStateOf(TextFieldValue("")) }
    val contents = listOf(
        "너무 잘생기셨어요",
        "무슨 치킨 좋아하세요?",
        "남자도 되나요?",
        "저랑 같이 먹어요 :)",
        "어디 사세요? ㅎㅎ",
        "저 기프티콘 있는데, 같이 드실래요?",
        "너무 잘생기셨어요",
        "무슨 치킨 좋아하세요?",
        "남자도 되나요?",
        "저랑 같이 먹어요 :)",
        "어디 사세요? ㅎㅎ",
        "저 기프티콘 있는데, 같이 드실래요?",
        "너무 잘생기셨어요",
        "무슨 치킨 좋아하세요?",
        "남자도 되나요?",
        "저랑 같이 먹어요 :)",
        "어디 사세요? ㅎㅎ",
        "저 기프티콘 있는데, 같이 드실래요?"
    )
    val colorList = listOf(
        listOf(Color(0xFFF5C1BD), Color(0xFFF0D8C3)),
        listOf(Color(0xFFAEC2E2), Color(0xFFAED6CC)),
        listOf(Color(0xFFBAD1F3), Color(0xFFF3F3F3)),
        listOf(Color(0xFFC7CACE), Color(0xFFF8EACD))
    )
    Scaffold(
        modifier = Modifier.padding(bottom = 48.dp),
    ) {
        Box(
            Modifier
                .padding(bottom = 10.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = colorList[1]
                    )
                )
        ) {
            Column(
                Modifier
                    .padding(20.dp, 15.dp, 20.dp, 15.dp)
                    .fillMaxSize()
            ) {
                Row(
                    Modifier.fillMaxWidth()
                        .height(40.dp)
                        .padding(end = 10.dp, bottom = 5.dp)
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "뒤로 가기 아이콘",
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 20.dp)
                        )
                    }
                    Text(
                        "댓글",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                        ),
                    )
                }
                Divider()
                Row(
                    Modifier
                        .padding(top = 15.dp, bottom = 15.dp)
                        .fillMaxWidth()
                        .height(550.dp)
                        .align(Alignment.CenterHorizontally),
                ) {
                    LazyColumn {
                        items(contents) { content ->
                            Column(
                            ) {
                                Row(
                                    Modifier.padding(top = 6.dp, bottom = 6.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.cha),
                                        contentDescription = "사람 사진",
                                        Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                    )
                                    Column(
                                        Modifier.alignByBaseline()
                                    ) {
                                        Text(
                                            "차은우",
                                            fontSize = 13.sp,
                                            modifier = Modifier.padding(start = 7.dp, bottom = 3.dp)
                                        )
                                        Text(
                                            content,
                                            fontSize = 13.sp,
                                            modifier = Modifier.padding(
                                                start = 7.dp,
                                                top = 3.dp,
                                                bottom = 5.dp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .height(80.dp)
                        .padding(end = 10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cha),
                        contentDescription = "사람 사진",
                        Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                            .padding(top = 6.dp)
                            .alignByBaseline()
                    )
                    OutlinedTextField(value = value, onValueChange = { value = it },
                        modifier = Modifier
                            .width(280.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.White),
                        placeholder = {
                            Text(
                                "댓글을 작성해주세요.",
                                style = TextStyle(fontSize = 12.sp, color = Color.DarkGray)
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 12.sp
                        )
                    )
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.paper_plane),
                            tint = Color.White,
                            contentDescription = "전송 아이콘",
                            modifier = Modifier
                                .size(35.dp)
                                .padding(start = 5.dp)
                        )
                    }
                }
            }
        }
    }
}