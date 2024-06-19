package com.example.weathervue.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weathervue.models.favouriteModel.Favourite

@Database(entities = [Favourite::class], version = 1, exportSchema = false)
abstract class CityDatabase: RoomDatabase() {

    abstract fun cityDao() : CityDao

}