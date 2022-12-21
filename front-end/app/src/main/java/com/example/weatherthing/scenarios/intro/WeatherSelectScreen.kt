package com.example.weatherthing.scenarios.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.example.weatherthing.utils.weatherPagerContent
import com.example.weatherthing.viewModel.StartViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WeatherSelectScreen(navController: NavHostController, viewModel: StartViewModel) {
    val pagerState = rememberPagerState()
    val weatherItems = weatherPagerContent()
    var selectedItem by remember {
        mutableStateOf(0)
    }
//    val lottieAnimatable = rememberLottieAnimatable()

    val compositionList = weatherItems.map {
        rememberLottieAnimatable() to rememberLottieComposition(
            spec =
            LottieCompositionSpec.Url(it.animationUrl)
        )
    }
    val colorList = listOf(
        listOf(Color(0xFFFF7167), Color(0xFFEEB685)),
        listOf(Color(0xFF4180E0), Color(0xFF7DD2BC)),
        listOf(Color(0xFF76AAF5), Color(0xFFF3F3F3)),
        listOf(Color(0xFF091525), Color(0xFFF5B534))
    )
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier.padding(it)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = colorList[pagerState.currentPage]
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                count = weatherItems.size,
                state = pagerState,
                modifier = Modifier.weight(1f)

            ) { currentPage ->

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        modifier = Modifier.size(200.dp).padding(vertical = 30.dp),
                        composition = compositionList[currentPage].second.value,
                        iterations = LottieConstants.IterateForever,
                        contentScale = ContentScale.Inside
                    )
                    Button(
                        onClick = {
                            navController.navigate("SignUp/$selectedItem")
                        },
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = weatherItems[selectedItem].title,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 45.sp,
                                fontStyle = FontStyle.Italic
                            )
                            Text(
                                text = "시작하기",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
//
                        }
                    }
                }
            }
            DotsIndicator(
                totalDots = 4,
                selectedIndex = pagerState.currentPage,
                selectedColor = Color(0xFFFFFFFF),
                unSelectedColor = Color(0x5BFEFFFF),
                modifier = Modifier.padding(bottom = 50.dp)
            )
        }
    }
    LaunchedEffect(Unit) {
        this.launch {
            scaffoldState.snackbarHostState.showSnackbar("당신의 날씨를 선택해주세요")
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedItem = pagerState.currentPage
    }

    LaunchedEffect(compositionList) {
        compositionList.forEach { (player, composition) ->
            player.animate(
                composition = composition.value,
                clipSpec = LottieClipSpec.Frame(0, 1200),
                initialProgress = 0f
            )
        }
    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color,
    unSelectedColor: Color,
    modifier: Modifier
) {
    LazyRow(
        modifier = modifier
    ) {
        items(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == selectedIndex) {
                            selectedColor
                        } else unSelectedColor
                    )
            )
            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 3.dp))
            }
        }
    }
}
