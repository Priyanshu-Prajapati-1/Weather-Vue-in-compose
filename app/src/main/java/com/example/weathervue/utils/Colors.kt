package com.example.weathervue.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush

object Colors {

    val textBrush = Brush.linearGradient(
        listOf(
            Color(0xE675EAFA),
            Color(0xCDFDFDFD),
        )
    )

    val gradientColors =
        Brush.linearGradient(
            listOf(
                Color(0xC178E9F8),
                Color(0xC4FDFDFD),
                Color(0xFF1E1E1E),
            )
        )

    val backgroundBrush =
        Brush.linearGradient(
            listOf(
                Color(0x30FDFDFD),
                Color(0x4500B8D4),
            )
        )


    val menuBackgroundBrush =
        Brush.linearGradient(
            listOf(
                Color(0x9E00B8D4),
                Color(0x9FFDFDFD),
            )
        )

    val splashBackgroundBrush =
        Brush.linearGradient(
            listOf(
                Color(0x7400B8D4),
                Color(0xEE1E1E1E),
            )
        )

    val rainbowBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFF7F00), // Orange
            Color(0xFFFFFF00), // Yellow
            Color(0xFF00FF00), // Green
        ),
        start = Offset.Zero,
        end = Offset.Infinite
    )
}