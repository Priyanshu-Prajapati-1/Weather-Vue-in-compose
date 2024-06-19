package com.example.weathervue.navigation

import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weathervue.screens.AboutScreen
import com.example.weathervue.screens.HomeScreen
import com.example.weathervue.screens.SplashScreen

const val time = 500


@Composable
fun WeatherNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WeatherScreens.SplashScreen.name,
        enterTransition = { scaleIn() },
        popEnterTransition = { scaleIn() },
    ) {

        composable(WeatherScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }

        composable(WeatherScreens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }
        composable(WeatherScreens.AboutScreen.name) {
            AboutScreen(navController = navController)
        }
    }

}