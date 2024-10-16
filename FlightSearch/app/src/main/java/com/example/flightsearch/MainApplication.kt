package com.example.flightsearch

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearch.data.FlightSearchDatabase
import com.example.flightsearch.data.UserPreferences

private const val DATA_STORE_NAME = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATA_STORE_NAME
)

class MainApplication: Application() {
    val database: FlightSearchDatabase by lazy {
        FlightSearchDatabase.getInstance(this)
    }
    lateinit var userPreferences: UserPreferences
    override fun onCreate() {
        super.onCreate()
        userPreferences = UserPreferences(dataStore)
    }
}