package com.example.weathervue.utils

fun checkAirQuality(
    aqi: Int
): String {

    return when {

        aqi in 0..50 -> "Good"
        aqi in 51..100 -> "Fair"
        aqi in 101..200 -> "Moderate"
        aqi in 200..300 -> "Poor"
        aqi in 301..400 -> "Very Poor"
        aqi in 401..500 -> "Severe"
        // Default case (if none of the ranges match)
        else -> "unknown"
    }
}