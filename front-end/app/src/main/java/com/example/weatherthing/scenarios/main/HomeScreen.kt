package com.example.weatherthing.scenarios

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.example.weatherthing.data.WeatherData
import com.example.weatherthing.utils.getWeatherAnimationUrl
import com.example.weatherthing.viewModel.MainViewModel
import com.example.weatherthing.viewModel.WeatherState

@Composable
fun Home(viewModel: MainViewModel, navController: NavHostController) {
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
                    ShowWeather(state.data)
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
                "${ (it - 273.15).toInt()}°C"
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
    Box(modifier = Modifier.fillMaxSize()) {
        LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever, contentScale = ContentScale.Crop)
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp, end = 10.dp, top = 10.dp), horizontalArrangement = Arrangement.End) {
                Icon(Icons.Default.LocationOn, contentDescription = "위치 아이콘")
                Text(text = weatherData.name ?: "", fontSize = 14.sp)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(end = 10.dp), horizontalArrangement = Arrangement.End) {
                Text(text = temp, fontSize = 14.sp)
            }
        }
    }
    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            composition = composition,
            clipSpec = LottieClipSpec.Frame(0, 1200),
            initialProgress = 0f
        )
    }
}
