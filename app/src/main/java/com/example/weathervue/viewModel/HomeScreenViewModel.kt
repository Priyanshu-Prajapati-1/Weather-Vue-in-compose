package com.example.weathervue.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathervue.data.DataOrException
import com.example.weathervue.data.Resource
import com.example.weathervue.models.airQuality.AirQuality
import com.example.weathervue.models.currentWeather.CurrentWeather
import com.example.weathervue.models.weatherModel.Weather
import com.example.weathervue.repository.ConnectivityRepository
import com.example.weathervue.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val connectivityRepository: ConnectivityRepository
) :
    ViewModel() {

    // Check internet connection
    val isOnline = connectivityRepository.isConnected

    val weatherData: StateFlow<DataOrException<Weather?, Boolean, Exception>>
        get() = repository.weatherData

    val currentWeatherData: StateFlow<DataOrException<CurrentWeather?, Boolean, Exception>>
        get() = repository.currentWeather

    val airQuality: StateFlow<Resource<AirQuality?>>
        get() = repository.airQuality

    var unit = mutableStateOf("metric")

    /* init {
         viewModelScope.launch {
             getWeatherData("Orai", unit.value)
         }
     }*/

    fun getCurrentWeather(lat: String, lon: String, unit: String) {
        viewModelScope.launch {
            getCurrentWeatherAndAirQuality(lat, lon, unit)
        }
    }

    suspend fun getWeatherData(city: String, unit: String) {
        repository.getWeather(city = city, unit = unit)
    }

    private suspend fun getCurrentWeatherAndAirQuality(lat: String, lon: String, unit: String) {
        repository.getCurrentWeather(lat, lon, unit)
        repository.getAirQuality(lat, lon, unit)
    }
}