package com.example.weathervue.repository

import com.example.weathervue.data.CityDao
import com.example.weathervue.models.favouriteModel.Favourite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherDatabaseRepository @Inject constructor(private val cityDao: CityDao) {

    fun getCities(): Flow<List<Favourite>> {
        return cityDao.getCities()
    }

    suspend fun insertCity(favourite: Favourite) = cityDao.insertCity(favourite)

    suspend fun deleteCity(favourite: Favourite) = cityDao.deleteCity(favourite)

    suspend fun updateCity(favourite: Favourite) = cityDao.updateFavorite(favourite)

    suspend fun deleteAllCities() = cityDao.deleteAllFavorite()

    suspend fun getCityById(city: String) = cityDao.getCityById(city)
}