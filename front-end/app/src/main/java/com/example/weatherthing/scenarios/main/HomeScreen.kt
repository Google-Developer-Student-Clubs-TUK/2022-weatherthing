package com.example.weatherthing.scenarios

import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherthing.R
import com.example.weatherthing.data.PostData
import com.example.weatherthing.data.WeatherData
import com.example.weatherthing.scenarios.main.NavItem
import com.example.weatherthing.ui.theme.skyblue
import com.example.weatherthing.utils.getWeatherAnimationUrl
import com.example.weatherthing.viewModel.MainViewModel
import com.example.weatherthing.viewModel.WeatherState

@Composable
fun Home(viewModel: MainViewModel, navController: NavHostController, weatherCode: Int) {
    Scaffold() {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            when (val state = viewModel.weather.collectAsState().value) {
                is WeatherState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is WeatherState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message)
                    }
                }
                is WeatherState.Loaded -> {
                    ShowBoard(state.data, navController, weatherCode)
                }
            }
        }
    }
}

@Composable
private fun ShowWeather(weatherData: WeatherData) {
    val temp by remember {
        mutableStateOf(
            weatherData.main?.temp?.let {
                "${(it - 273.15).toInt()}°C"
            } ?: "-°C"
        )
    }
    val aniUrl by remember {
        mutableStateOf(getWeatherAnimationUrl(weatherData.weather?.get(0)?.icon))
    }
    Log.d("zini", aniUrl)
    val composition by rememberLottieComposition(
        spec =
        LottieCompositionSpec.Url(aniUrl)
    )
    val lottieAnimatable = rememberLottieAnimatable()
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(top = 15.dp)
            ) {
                Text(
                    "WeatherThing",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.alignByBaseline()
                )
                Column() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = "위치 아이콘")
                        Text(text = weatherData.name ?: "", fontSize = 14.sp)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp), horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = temp, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowBoard(weatherData: WeatherData, navController: NavHostController, weatherCode: Int) {
    val contents = listOf(
        "치킨 먹고 싶은데 야식으로 같이 드실 분 계실까요?",
        "근처 맛집 추천해주세요 ㅎㅎ",
        "다들 볼링 좋아하시나요?",
        "썸남 심리 상담해주실 분 ㅠㅠ",
        "정왕동 병원 잘하는 곳 아시나요? 목이 너무 아픈데",
        "저희 집 고양이 보시고 가세요",
        "사기 조심하세요\n여자분이랑 약속 잡고 나갔더니 건장한 남성이었어요 ㅡㅡ",
        "다이어트 중인데 배고프네요",
        "첫 데이트로 하남돼지집 괜찮나요?"
    )
    val image = listOf(
        R.drawable.cha,
        R.drawable.winter,
        R.drawable.cat,
        R.drawable.card,
        R.drawable.jjang_gu,
        R.drawable.door,
        R.drawable.health,
        R.drawable.umm,
        R.drawable.fish
    )

    val nicknames = listOf(
        "차은우",
        "윈터",
        "냥냥펀치",
        "카드값줘체리",
        "짱구는 옷말려",
        "문희 열립니다",
        "헬창",
        "하울의 음~쥑이는 성",
        "생갈치1호의행방불명"
    )

    var index = 0

    val post = mutableListOf<PostData>().apply {
        repeat(9){
            add(PostData(nicknames[index], image[index], contents[index]))
            index += 1
        }
    }

    val colorList = listOf(
        listOf(Color(0xFFF3A19C), Color(0xFFEEBC90)),
        listOf(Color(0xFF8EB0E6), Color(0xFF9FD5C7)),
        listOf(Color(0xFF9CC0F5), Color(0xFFF3F3F3)),
        listOf(Color(0xFF8E9399), Color(0xFFF7D89A))
    )
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.padding(bottom = 48.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDropDownMenuExpanded = true },
                contentColor = Color.Black,
                backgroundColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_add_24),
                    contentDescription = "add",
                    modifier = Modifier.size(30.dp),
                    tint = skyblue
                )
                DropdownMenu(
                    modifier = Modifier.wrapContentSize(),
                    expanded = isDropDownMenuExpanded,
                    onDismissRequest = { isDropDownMenuExpanded = false }
                ) {
                    DropdownMenuItem(onClick = { navController.navigate(NavItem.POST.routeName) }) {
                        Text("게시물 작성")
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
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
                modifier = Modifier
                    .padding(20.dp, 0.dp, 20.dp, 0.dp)
            ) {
                ShowWeather(weatherData)
                Text(
                    "자유게시판",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                LazyColumn {
                    itemsIndexed(post) { _, post ->
                        postItemView(post, navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun postItemView(post: PostData, navController: NavHostController) {
    Divider()
    Column(
    ) {
        Row(
            Modifier.padding(top = 5.dp)
        ) {
            Image(
                painter = painterResource(id = post.image),
                contentDescription = "사람 사진",
                Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Text(
                post.nickname,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 7.dp, bottom = 3.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        Text(
            post.content,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(top = 13.dp, bottom = 5.dp)
                .clickable { navController.navigate(NavItem.COMMENT.routeName) }
        )
    }
}