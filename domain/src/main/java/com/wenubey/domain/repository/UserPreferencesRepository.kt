package com.wenubey.domain.repository


import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    val isLinearLayout: Flow<Boolean>

    val isNightMode: Flow<Boolean>

    val isScreenLocked: Flow<Boolean>

    suspend fun saveLayoutPreference(isLinearLayout: Boolean)

    suspend fun saveNightModePreference(isNightMode: Boolean)

    suspend fun saveScreenLockedPreference(isScreenLocked: Boolean)

}