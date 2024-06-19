package com.example.weathervue.network

import com.example.weathervue.models.airQuality.AirQuality
import com.example.weathervue.models.currentWeather.CurrentWeather
import com.example.weathervue.models.weatherModel.Weather
import com.example.weathervue.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET(value = "data/2.5/forecast/daily")
    suspend fun getWeather(
        @Query("q") query: String,
        @Query("units") units: String = "metric",
        @Query("cnt") cnt: Int = 16,
        @Query("appid") appid: String = Constants.API_KEY
    ): Response<Weather>

    @GET(value = "data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = Constants.MY_API_KEY
    ): Response<CurrentWeather>

    @GET(value = "data/2.5/air_pollution")
    suspend fun getAirQuality(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = Constants.MY_API_KEY
    ): Response<AirQuality>

}