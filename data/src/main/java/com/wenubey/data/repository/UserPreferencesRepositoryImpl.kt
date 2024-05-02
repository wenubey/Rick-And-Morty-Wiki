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

    override val characterSearchHistory: Flow<List<String>>
        get() = dataStore.data
            .catch {
                Log.e(TAG, "Error reading character search history:", it)
            }.map { preferences ->
                preferences[CHARACTER_SEARCH_HISTORY]?.split(",") ?: listOf()
            }

    override val locationSearchHistory: Flow<List<String>>
        get() = dataStore.data
            .catch {
                Log.e(TAG, "Error reading location search history:", it)
            }.map { preferences ->
                preferences[LOCATION_SEARCH_HISTORY]?.split(",") ?: listOf()
            }

    override val isTopBarLocked: Flow<Boolean>
        get() = dataStore.data
            .catch {
                Log.e(TAG, "Error reading top bar preference:", it)
            }.map { preferences ->
                preferences[IS_TOP_BAR_LOCKED] ?: false
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

    override suspend fun saveCharacterSearchHistory(searchQuery: String) {
        dataStore.edit { preferences ->
            val currentHistory =
                preferences[CHARACTER_SEARCH_HISTORY]?.split(",")?.toMutableList() ?: mutableListOf()
            Log.i(TAG, "saveCharacterSearchHistory:SEARCH QUERY: $searchQuery")
            Log.i(TAG, "saveCharacterSearchHistory:SEARCH QUERY is not blank: ${searchQuery.isNotBlank()}")
            if (searchQuery.isNotBlank()) {
                currentHistory.add(0, searchQuery)
            }
            if (currentHistory.size > 10) {
                currentHistory.removeLast()
            }

            preferences[CHARACTER_SEARCH_HISTORY] = currentHistory.joinToString(",")
        }
    }

    override suspend fun saveLocationSearchHistory(searchQuery: String) {
        dataStore.edit { preferences ->
            val currentHistory =
                preferences[LOCATION_SEARCH_HISTORY]?.split(",")?.toMutableList() ?: mutableListOf()

            if (searchQuery.isNotBlank()) {
                currentHistory.add(0, searchQuery)
            }
            if (currentHistory.size > 10) {
                currentHistory.removeLast()
            }

            preferences[CHARACTER_SEARCH_HISTORY] = currentHistory.joinToString(",")
        }
    }

    override suspend fun cleanAllSearchHistory() {
        dataStore.edit { preferences ->
            preferences.remove(LOCATION_SEARCH_HISTORY)
            preferences.remove(CHARACTER_SEARCH_HISTORY)
        }
    }

    override suspend fun saveTopBarLockedPreference(isTopBarLocked: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_TOP_BAR_LOCKED] = isTopBarLocked
        }
    }


    private companion object {
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
        val IS_NIGHT_MODE = booleanPreferencesKey("is_night_mode")
        val IS_SCREEN_LOCKED = booleanPreferencesKey("is_screen_locked")
        val IS_TOP_BAR_LOCKED = booleanPreferencesKey("is_top_bar_locked")
        val CHARACTER_SEARCH_HISTORY = stringPreferencesKey("character_search_history")
        val LOCATION_SEARCH_HISTORY = stringPreferencesKey("location_search_history")
        const val TAG = "UserPreferencesRepo"
    }
}