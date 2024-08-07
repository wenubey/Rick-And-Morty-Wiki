package com.wenubey.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val ioDispatcher: CoroutineDispatcher
) : SettingsRepository {

    override val isLinearLayout: Flow<Boolean>
        get() = dataStore.data
            .catch {
                Timber.e(it, "Error reading isLinearLayout preference:")
            }
            .map { preferences ->
                preferences[IS_LINEAR_LAYOUT] ?: true
            }

    override val isNightMode: Flow<Boolean>
        get() = dataStore.data
            .catch {
                Timber.e(it, "Error reading isNightMode preference:")
            }
            .map { preferences ->
                preferences[IS_NIGHT_MODE] ?: true
            }

    override val isScreenLocked: Flow<Boolean>
        get() = dataStore.data
            .catch {
                Timber.e(it, "Error reading isScreenLocked preference:")
            }
            .map { preferences ->
                preferences[IS_SCREEN_LOCKED] ?: false
            }

    override val isTopBarLocked: Flow<Boolean>
        get() = dataStore.data
            .catch {
                Timber.e(it, "Error reading top bar preference:")
            }.map { preferences ->
                preferences[IS_TOP_BAR_LOCKED] ?: false
            }

    override fun getSearchHistory(dataTypeKey: DataTypeKey): Flow<List<String>> =
        dataStore.data
            .catch {
                Timber.e(it, "Error reading search history")
            }.map { preferences ->
                val preferenceKey = getPreferenceKeyFromType(dataTypeKey)
                preferences[preferenceKey]?.let {
                    Json.decodeFromString<List<String>>(it)
                } ?: emptyList()
            }


    override suspend fun saveLayoutPreference(isLinearLayout: Boolean) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[IS_LINEAR_LAYOUT] = isLinearLayout
            }
        }
    }

    override suspend fun saveNightModePreference(isNightMode: Boolean)  {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[IS_NIGHT_MODE] = isNightMode
            }
        }
    }

    override suspend fun saveScreenLockedPreference(isScreenLocked: Boolean) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[IS_SCREEN_LOCKED] = isScreenLocked
            }
        }
    }

    override suspend fun saveSearchHistory(dataTypeKey: DataTypeKey, searchQuery: String) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                val preferenceKey = getPreferenceKeyFromType(dataTypeKey)
                val currentHistory =
                    preferences[preferenceKey]?.let {
                        Json.decodeFromString<List<String>>(it)
                            .toMutableList()
                    } ?: mutableListOf()
                if (searchQuery.isNotBlank()) {
                    currentHistory.add(0, searchQuery)
                }
                if (currentHistory.size > 7) {
                    currentHistory.removeLast()
                }
                preferences[preferenceKey] = Json.encodeToString(currentHistory)
            }
        }
    }

    override suspend fun cleanAllSearchHistory() {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences.remove(LOCATION_SEARCH_HISTORY)
                preferences.remove(CHARACTER_SEARCH_HISTORY)
            }
        }
    }

    override suspend fun saveTopBarLockedPreference(isTopBarLocked: Boolean) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[IS_TOP_BAR_LOCKED] = isTopBarLocked
            }
        }
    }

    private fun getPreferenceKeyFromType(dataTypeKey: DataTypeKey): Preferences.Key<String> {
        return when(dataTypeKey) {
            DataTypeKey.LOCATION -> LOCATION_SEARCH_HISTORY
            DataTypeKey.CHARACTER -> CHARACTER_SEARCH_HISTORY
        }
    }


    private companion object {
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
        val IS_NIGHT_MODE = booleanPreferencesKey("is_night_mode")
        val IS_SCREEN_LOCKED = booleanPreferencesKey("is_screen_locked")
        val IS_TOP_BAR_LOCKED = booleanPreferencesKey("is_top_bar_locked")
        val CHARACTER_SEARCH_HISTORY = stringPreferencesKey("character_search_history")
        val LOCATION_SEARCH_HISTORY = stringPreferencesKey("location_search_history")
    }
}