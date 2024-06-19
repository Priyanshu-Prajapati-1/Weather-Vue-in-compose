package com.example.weathervue.models.airQuality

data class AirQuality(
    val coord: Coord,
    val list: List<AirQualityDetails>
)