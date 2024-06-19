package com.example.weathervue.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weathervue.R
import com.example.weathervue.navigation.WeatherScreens
import com.example.weathervue.viewModel.HomeScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier, navController: NavHostController,
) {

    val scale = remember {
        Animatable(0f)
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_weather))

    // Animation
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.9f,
            // tween Animation
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )
        // Customize the delay time
        delay(500L)
        navController.navigate(WeatherScreens.HomeScreen.name) {
            popUpTo(0)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LottieAnimation(
            composition = composition,
            iterations = 1,//Int.SIZE_BITS,
            speed = 1.5f,
            modifier = Modifier
                .scale(scale.value)
                .align(Alignment.Center)
        )

        Text(
            text = "Weather Vue",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .systemBarsPadding()
                .padding(bottom = 30.dp),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

}