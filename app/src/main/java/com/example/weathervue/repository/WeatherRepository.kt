package com.example.weathervue.repository

import com.example.weathervue.data.DataOrException
import com.example.weathervue.data.Resource
import com.example.weathervue.models.airQuality.AirQuality
import com.example.weathervue.models.currentWeather.CurrentWeather
import com.example.weathervue.models.weatherModel.Weather
import com.example.weathervue.network.WeatherAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherAPI: WeatherAPI) {

    private var _weatherData = MutableStateFlow<DataOrException<Weather?, Boolean, Exception>>(
        DataOrException(
            null,
            true, Exception("")
        )
    )
    val weatherData: StateFlow<DataOrException<Weather?, Boolean, Exception>>
        get() = _weatherData

    suspend fun getWeather(city: String, unit: String) {
        _weatherData.value = DataOrException(loading = true)
        try {
            val response = weatherAPI.getWeather(query = city, units = unit)
            if (response.isSuccessful) {
                _weatherData.value = DataOrException(
                    loading = false,
                    data = response.body(),
                    e = Exception(response.body()?.cod)
                )
            } else {
                _weatherData.value =
                    DataOrException(loading = false, e = Exception(response.message()))
            }
        } catch (e: Exception) {
            _weatherData.value = DataOrException(loading = false, e = e)
        }
    }

    private val _currentWeather =
        MutableStateFlow<DataOrException<CurrentWeather?, Boolean, Exception>>(
            DataOrException(
                null,
                true, Exception("")
            )
        )
    val currentWeather: StateFlow<DataOrException<CurrentWeather?, Boolean, Exception>>
        get() = _currentWeather

    suspend fun getCurrentWeather(lat: String, lon: String, unit: String) {
        _currentWeather.value = DataOrException(loading = true)
        try {
            val weatherResponse = weatherAPI.getCurrentWeather(lat = lat, lon = lon, units = unit)
            if (weatherResponse.isSuccessful) {
                _currentWeather.value = DataOrException(
                    loading = false,
                    data = weatherResponse.body(),
                    e = Exception(weatherResponse.body()?.cod.toString())
                )
            } else {
                _currentWeather.value =
                    DataOrException(loading = false, e = Exception(weatherResponse.message()))
            }
        } catch (e: Exception) {
            _currentWeather.value = DataOrException(loading = false, e = e)
        }
    }

    private val _airQuality = MutableStateFlow<Resource<AirQuality?>>(
       Resource.Loading()
    )

    val airQuality: StateFlow<Resource<AirQuality?>>
        get() = _airQuality

    suspend fun getAirQuality(lat: String, lon: String, unit: String) {
        try {
            Resource.Loading(data = true)
            val getAirQuality = weatherAPI.getAirQuality(lat = lat, lon = lon, units = unit)
            if(getAirQuality.isSuccessful){
                _airQuality.value = Resource.Success(data = getAirQuality.body())
            }else{
                _airQuality.value = Resource.Error(message = "Error", data = null)
            }
        }catch (e: Exception){
            _airQuality.value = Resource.Error(message = e.message.toString(), data = null)
        }
    }
}

// Weather(city=City(coord=Coord(lat=51.5085, lon=-0.1257), country=GB, id=2643743, name=London, population=1000000, timezone=3600), cnt=16, cod=200, list=[WeatherItem(clouds=100, deg=119, dt=1715511600, feels_like=FeelsLike(day=22.08, eve=20.55, morn=12.14, night=15.91), gust=10.04, humidity=55, pop=0.86, pressure=1013, rain=1.95, speed=4.08, sunrise=1715487164, sunset=1715542865, temp=Temp(day=22.35, eve=20.37, max=24.22, min=12.26, morn=12.51, night=15.82), weather=[WeatherObject(description=light rain, icon=10d, id=500, main=Rain)]), WeatherItem(clouds=74, deg=177, dt=1715598000, feels_like=FeelsLike(day=17.37, eve=16.86, morn=12.06, night=14.25), gust=11.17, humidity=67, pop=0.69, pressure=1007, rain=0.69, speed=6.14, sunrise=1715573471, sunset=1715629358, temp=Temp(day=17.79, eve=17.23, max=19.57, min=12.29, morn=12.29, night=14.31), weather=[WeatherObject(description=light rain, icon=10d, id=500, main=Rain)]), WeatherItem(clouds=100, deg=164, dt=1715684400, feels_like=FeelsLike(day=14.96, eve=14.7, morn=13.67, night=12.53), gust=10.28, humidity=72, pop=1.0, pressure=999, rain=14.38, speed=5.81, sunrise=1715659781, sunset=1715715850, temp=Temp(day=15.48, eve=15.0, max=15.86, min=11.8, morn=13.69, night=12.79), weather=[WeatherObject(description=moderate rain, icon=10d, id=501, main=Rain)]), WeatherItem(clouds=99, deg=143, dt=1715770800, feels_like=FeelsLike(day=16.03, eve=14.8, morn=11.04, night=12.13), gust=9.69, humidity=60, pop=0.28, pressure=1003, rain=0.26, speed=5.0, sunrise=1715746092, sunset=1715802342, temp=Temp(day=16.73, eve=15.4, max=16.73, min=10.89, morn=11.51, night=12.64), weather=[WeatherObject(description=light rain, icon=10d, id=500, main=Rain)]), WeatherItem(clouds=73, deg=145, dt=1715857200, feels_like=FeelsLike(day=15.62, eve=15.46, morn=11.43, night=11.98), gust=8.74, humidity=65, pop=0.33, pressure=1007, rain=1.01, speed=3.11, sunrise=1715832406, sunset=1715888832, temp=Temp(day=16.24, eve=15.93, max=17.3, min=10.63, morn=11.91, night=12.36), weather=[WeatherObject(description=light rain, icon=10d, id=500, main=Rain)]), WeatherItem(clouds=74, deg=160, dt=1715943600, feels_like=FeelsLike(day=16.86, eve=16.52, morn=11.42, night=13.53), gust=4.18, humidity=71, pop=0.86, pressure=1010, rain=2.11, speed=3.91, sunrise=1715918721, sunset=1715975321, temp=Temp(day=17.23, eve=17.04, max=18.86, min=10.31, morn=11.88, night=13.92), weather=[WeatherObject(description=light rain, icon=10d, id=500, main=Rain)]), WeatherItem(clouds=46, deg=48, dt=1716030000, feels_like=FeelsLike(day=18.97, eve=15.12, morn=12.13, night=12.87), gust=9.9, humidity=54, pop=0.76, pressure=1012, rain=3.69, speed=5.13, sunrise=1716005039, sunset=1716061810, temp=Temp(day=19.55, eve=15.22, max=19.55, min=11.49, morn=12.71, night=13.03), weather=[WeatherObject(description=light rain, icon=10d, id=500, main=Rain)]), WeatherItem(clouds=88, deg=134, dt=1716116400, feels_like=FeelsLike(day=15.51, eve=14.58, morn=12.35, night=12.08), gust=8.67, humidity=75, pop=0.96, pressure=1006, rain=5.47, speed=4.91, sunrise=1716091359, sunset=1716148297, temp=Temp(day=15.91, eve=14.73, max=15.91, min=12.29, morn=12.53, night=12.29), weather=[WeatherObject(description=light rain, icon=10d, id=500, main=Rain)]), WeatherItem(clouds=95, deg=214, dt=1716202800, feels_like=FeelsLike(day=16.96, eve=16.2, morn=11.6, night=12.39), gust=5.8, humidity=67, pop=0.78, pressure=1007, rain=1.46, speed=3.81, sunrise=1716177681, sunset=1716234783, temp=Temp(day=17.41, eve=16.7, max=17.41, min=10.54, morn=11.95, night=12.81), weather=[WeatherObject(description=light rain, icon=10d, id=500, main=Rain)]), WeatherItem(clouds=90, deg=200, dt=1716289200, feels_like=FeelsLike(day=16.31, eve=15.91, morn=11.77, night=12.42), gust=5.85, humidity=78, pop=0.88, pressure=1013, rain=4.98, speed=2.55, sunrise=1716264005, sunset=1716321267, temp=Temp(day=16.56, eve=16.06, max=17.06, min=10.43, morn=12.24, night=12.62), weather=[WeatherObject(description=light rain, icon=10d, id=500, main=Rain)]), WeatherItem(clouds=33, deg=141, dt=1716375600, feel
