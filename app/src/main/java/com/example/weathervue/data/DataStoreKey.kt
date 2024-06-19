package com.example.weathervue.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreKey(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "unit_setting")
        private val Context.cityStore: DataStore<Preferences> by preferencesDataStore(name = "city_setting")
    }

    fun getDataStoreValue(key: String): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: "metric"
        }
    }

    fun getCityStoreValue(key: String): Flow<String?> {
        return context.cityStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: "orai"
        }
    }

    suspend fun saveToDataStore(key: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun saveToCityStore(key: String, value: String) {
        context.cityStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }
}