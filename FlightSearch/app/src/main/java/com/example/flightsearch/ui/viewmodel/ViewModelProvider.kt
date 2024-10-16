package com.example.flightsearch.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.MainApplication

object ViewModelsProvider {
    val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val application = (this[APPLICATION_KEY] as MainApplication)
            ViewModelFlightSearch(
                flightSearchDao = application.database.flightSearchDao(),
                userPreferences = application.userPreferences
            )
        }
    }
}