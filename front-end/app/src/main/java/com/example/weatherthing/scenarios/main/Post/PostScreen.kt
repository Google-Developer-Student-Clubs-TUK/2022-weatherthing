package com.example.weatherthing.scenarios.main.Post

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.weatherthing.R
import com.example.weatherthing.scenarios.main.BottomNavItem
import com.example.weatherthing.ui.theme.sky
import com.example.weatherthing.ui.theme.skyblue

@Composable
fun PostScreen(navController: NavHostController) {
    var value by remember { mutableStateOf(TextFieldValue("")) }
    val colorList = listOf(
        listOf(Color(0xFFF5C1BD), Color(0xFFF0D8C3)),
        listOf(Color(0xFFAEC2E2), Color(0xFFAED6CC)),
        listOf(Color(0xFFBAD1F3), Color(0xFFF3F3F3)),
        listOf(Color(0xFFC7CACE), Color(0xFFF8EACD))
    )
    Scaffold() {
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
                Row() {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                        "WeatherThing",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic
                    )
                }
                Row(
                    Modifier.padding(top = 15.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cha),
                        contentDescription = "사람 사진",
                        Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        "차은우",
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 7.dp, bottom = 3.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
                OutlinedTextField(value = value, onValueChange = { value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(top = 10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.White),
                    placeholder = {
                        Text(
                            "게시물을 작성해주세요.",
                            style = TextStyle(fontSize = 13.sp, color = Color.DarkGray)
                        )
                    }
                )
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    border = BorderStroke(1.dp, sky),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.elevation(0.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "등록",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}