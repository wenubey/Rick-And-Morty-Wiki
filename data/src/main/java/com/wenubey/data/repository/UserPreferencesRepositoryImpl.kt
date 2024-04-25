package com.wenubey.data.repository


import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wenubey.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : UserPreferencesRepository {

    override val isLinearLayout: Flow<Boolean>
        get() = dataStore.data
            .catch {
                Log.e(TAG, "Error reading isLinearLayout preference:", it)
            }
            .map { preferences ->
                preferences[IS_LINEAR_LAYOUT] ?: true
            }

    override val isNightMode: Flow<Boolean>
        get() = dataStore.data
            .catch {
                Log.e(TAG, "Error reading isNightMode preference:", it)
            }
            .map { preferences ->
                preferences[IS_NIGHT_MODE] ?: true
            }

    override val isScreenLocked: Flow<Boolean>
        get() = dataStore.data
            .catch {
                Log.e(TAG, "Error reading isScreenLocked preference:", it)
            }
            .map { preferences ->
                preferences[IS_SCREEN_LOCKED] ?: false
            }

    override val searchHistory: Flow<List<String>>
        get() = dataStore.data
            .catch {
                Log.e(TAG, "Error reading searchHistory:", it)
            }.map { preferences ->
                preferences[SEARCH_HISTORY]?.split(",") ?: listOf()
            }

    override suspend fun saveLayoutPreference(isLinearLayout: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LINEAR_LAYOUT] = isLinearLayout
        }
    }

    override suspend fun saveNightModePreference(isNightMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_NIGHT_MODE] = isNightMode
        }
    }

    override suspend fun saveScreenLockedPreference(isScreenLocked: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_SCREEN_LOCKED] = isScreenLocked
        }
    }

    override suspend fun saveSearchHistory(searchQuery: String) {
        dataStore.edit { preferences ->
            val currentHistory =
                preferences[SEARCH_HISTORY]?.split(",")?.toMutableList() ?: mutableListOf()

            currentHistory.add(0, searchQuery)
            if (currentHistory.size > 10) {
                currentHistory.removeLast()
            }

            preferences[SEARCH_HISTORY] = currentHistory.joinToString(",")
        }
    }


    private companion object {
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
        val IS_NIGHT_MODE = booleanPreferencesKey("is_night_mode")
        val IS_SCREEN_LOCKED = booleanPreferencesKey("is_screen_locked")
        val SEARCH_HISTORY = stringPreferencesKey("search_history")
        const val TAG = "UserPreferencesRepo"
    }
}