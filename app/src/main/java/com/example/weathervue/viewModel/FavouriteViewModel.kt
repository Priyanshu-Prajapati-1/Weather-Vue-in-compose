package com.example.weathervue.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathervue.models.favouriteModel.Favourite
import com.example.weathervue.repository.WeatherDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val weatherDatabaseRepository: WeatherDatabaseRepository) :
    ViewModel() {

    private val _favList = MutableStateFlow<List<Favourite>>(emptyList())
    val favList = _favList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            weatherDatabaseRepository.getCities().distinctUntilChanged()
                .collect { listOfFavs ->
                    if (listOfFavs.isNotEmpty()) {
                        _favList.value = listOfFavs
                        Log.d("favList: ", "favList: $listOfFavs")
                    } else {
                        Log.d("favList: ", "favList: $listOfFavs")
                    }
                }
        }
    }

    fun insertCity(favourite: Favourite) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherDatabaseRepository.insertCity(favourite)
        }
    }

    fun deleteCity(favourite: Favourite) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherDatabaseRepository.deleteCity(favourite)
        }
    }

}