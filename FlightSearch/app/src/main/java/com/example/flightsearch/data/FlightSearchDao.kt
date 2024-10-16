package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightSearchDao {
    @Insert
    suspend fun insertFavoriteRoute(favorite: Favorite)

    @Delete
    suspend fun deleteFavoriteRoute(favorite: Favorite)

    @Query("SELECT * FROM favorite")
    fun getAllFavorite(): Flow<List<Favorite>>

    @Query("SELECT * FROM airport")
    fun getAllAirport(): Flow<List<Airport>>
}