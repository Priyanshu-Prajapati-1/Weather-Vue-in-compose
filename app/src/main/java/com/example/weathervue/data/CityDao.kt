package com.example.weathervue.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weathervue.models.favouriteModel.Favourite
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM fav_city_tbl")
    fun getCities(): Flow<List<Favourite>>

    @Query("SELECT * FROM fav_city_tbl WHERE city = :city")
    suspend fun getCityById(city: String): Favourite

    @Delete
    suspend fun deleteCity(favorite: Favourite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(favourite: Favourite)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavorite(favorite: Favourite)

    @Query("DELETE FROM fav_city_tbl")
    suspend fun deleteAllFavorite()
}