package com.example.weathervue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weathervue.navigation.WeatherNavigation
import com.example.weathervue.ui.theme.WeatherVueTheme
import com.example.weathervue.utils.Colors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherVueTheme(
                dynamicColor = false,
                darkTheme = true
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                        .background(brush = Colors.splashBackgroundBrush)
                ) {
                    WeatherApp()
                }
            }
        }
    }
}

@Composable
private fun WeatherApp() {
    WeatherNavigation()
}