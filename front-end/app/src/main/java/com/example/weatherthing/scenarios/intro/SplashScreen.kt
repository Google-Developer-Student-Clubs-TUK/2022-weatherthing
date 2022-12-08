package com.example.weatherthing.scenarios.intro

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.*

@Composable
fun SplashScreen() {
    val rainComposition by rememberLottieComposition(
        spec =
        LottieCompositionSpec.Url("https://assets8.lottiefiles.com/packages/lf20_h0cc4ii6.json")
    )
    val heartComposition by rememberLottieComposition(
        spec =
        LottieCompositionSpec.Url("https://assets2.lottiefiles.com/packages/lf20_YEZz8Y.json")
    )
    val lottieAnimatable = rememberLottieAnimatable()
    Surface(modifier = Modifier.fillMaxSize()) {
        LottieAnimation(
            composition = rainComposition,
            iterations = LottieConstants.IterateForever,
            contentScale = ContentScale.Crop
        )
        LottieAnimation(
            composition = heartComposition,
            iterations = LottieConstants.IterateForever,
            contentScale = ContentScale.Inside
        )
    }
    LaunchedEffect(rainComposition) {
        lottieAnimatable.animate(
            composition = rainComposition,
            clipSpec = LottieClipSpec.Frame(0, 1200),
            initialProgress = 0f
        )
    }
    LaunchedEffect(heartComposition) {
        lottieAnimatable.animate(
            composition = heartComposition,
            clipSpec = LottieClipSpec.Frame(0, 1200),
            initialProgress = 0f
        )
    }
}
