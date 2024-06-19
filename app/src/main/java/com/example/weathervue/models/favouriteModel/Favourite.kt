package com.example.weathervue.models.favouriteModel

import androidx.annotation.NonNull
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "fav_city_tbl")
data class Favourite(

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "city")
    val city: String,

    @ColumnInfo(name = "country")
    val country: String
)
