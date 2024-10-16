package com.example.flightsearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FlightSearchDao
import com.example.flightsearch.data.UserPreferences
import com.example.flightsearch.ui.model.FlightSearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelFlightSearch(
    private val flightSearchDao: FlightSearchDao,
    private val userPreferences: UserPreferences
): ViewModel() {
    //Lectura y escritura del estado
    private val _uiState = MutableStateFlow(FlightSearchUiState())
    val uiState: StateFlow<FlightSearchUiState> = _uiState.asStateFlow()
    //Estado de DataStore
    val saveText: StateFlow<String> = userPreferences.saveText
        .map{ it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )
    //Cambia el texto de busqueda
    fun setSearchText(searchText: String) {
        _uiState.update { data -> data.copy(searchText = searchText) }
    }
    //Cambia el estado de la busqueda
    fun setSearchOk(searchOk: Boolean) {
        _uiState.update { data -> data.copy(searchOk = searchOk) }
    }
    //Guarda el texto de busqueda
    fun saveSearchText(saveText: String) = viewModelScope.launch {
        userPreferences.saveSearchText(saveText)
    }
    //Obtiene todas las rutas favoritas
    fun getAllFavorite() = flightSearchDao.getAllFavorite()
    //Obtiene los aeropuertos por codigo o nombre
    fun getAllAirport() = flightSearchDao.getAllAirport()
    //Inserta una ruta favorita
    fun insertFavoriteRoute(favorite: Favorite) = viewModelScope.launch {
        flightSearchDao.insertFavoriteRoute(favorite)
    }
    //Borra una ruta favorita
    fun deleteFavoriteRoute(favorite: Favorite) = viewModelScope.launch {
        flightSearchDao.deleteFavoriteRoute(favorite)
    }
}