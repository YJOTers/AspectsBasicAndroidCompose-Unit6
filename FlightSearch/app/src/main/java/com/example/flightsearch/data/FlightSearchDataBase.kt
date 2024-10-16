package com.example.flightsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Airport::class, Favorite::class], version = 1)
abstract class FlightSearchDatabase : RoomDatabase(){
    abstract fun flightSearchDao(): FlightSearchDao

    companion object {
        private const val DATABASE_NAME = "flight_search_db"
        @Volatile
        private var instance: FlightSearchDatabase? = null

        fun getInstance(context: Context): FlightSearchDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    FlightSearchDatabase::class.java,
                    DATABASE_NAME
                )
                .createFromAsset("database/flight_search.db")
                .build()
                .also { instance = it }
            }
        }
    }
}