package com.example.weatherthing.scenarios.intro

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.example.weatherthing.utils.weatherPagerContent
import com.example.weatherthing.viewModel.StartViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Composable
fun WeatherSelectScreen(navController: NavHostController, viewModel: StartViewModel) {
    Box(modifier = Modifier.fillMaxSize().padding(20.dp), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "어떤 날씨가 떠오르시나요?\n당신의 날씨를 선택해주세요!",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            WeatherPager(navController, viewModel)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ColumnScope.WeatherPager(navController: NavHostController, viewModel: StartViewModel) {
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

    HorizontalPager(
        count = weatherItems.size,
        state = pagerState,
        modifier = Modifier.padding(30.dp).weight(1f)
    ) { currentPage ->
        Card(
            backgroundColor = Color.White,
            shape = MaterialTheme.shapes.large,
            elevation = 7.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            LottieAnimation(
                composition = compositionList[currentPage].second.value,
                iterations = LottieConstants.IterateForever,
                contentScale = ContentScale.Inside
            )
        }
    }

    Button(
        onClick = {
            navController.navigate("SignUp/$selectedItem")
        },
        colors = ButtonDefaults.buttonColors(Color.White)
    ) {
        Text(text = "\'${weatherItems[selectedItem].title}\'${if (selectedItem % 2 == 0)"으로" else "로"} 시작하기${weatherItems[selectedItem].icon}")
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
