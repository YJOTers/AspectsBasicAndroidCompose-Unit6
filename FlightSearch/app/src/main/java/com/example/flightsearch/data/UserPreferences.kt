package com.example.flightsearch.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferences(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val SEARCH_TEXT = stringPreferencesKey("search_text")
        const val TAG = "UserPreferences"
    }
    val saveText: Flow<String> = dataStore.data
        .catch {
            if(it is IOException){
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else{ throw it }
        }
        .map { preferences ->
            preferences[SEARCH_TEXT] ?: ""
        }

    suspend fun saveSearchText(saveText: String){
        dataStore.edit { preferences ->
            preferences[SEARCH_TEXT] = saveText
        }
    }
}