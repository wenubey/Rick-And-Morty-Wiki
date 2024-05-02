package com.wenubey.domain.repository


import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    val isLinearLayout: Flow<Boolean>

    val isNightMode: Flow<Boolean>

    val isScreenLocked: Flow<Boolean>

    val characterSearchHistory: Flow<List<String>>

    val locationSearchHistory: Flow<List<String>>

    val isTopBarLocked: Flow<Boolean>

    suspend fun saveLayoutPreference(isLinearLayout: Boolean)

    suspend fun saveNightModePreference(isNightMode: Boolean)

    suspend fun saveScreenLockedPreference(isScreenLocked: Boolean)

    suspend fun saveTopBarLockedPreference(isTopBarLocked: Boolean)

    suspend fun saveCharacterSearchHistory(searchQuery: String)

    suspend fun saveLocationSearchHistory(searchQuery: String)

    suspend fun cleanAllSearchHistory()

}