package com.wenubey.domain.repository


import com.wenubey.domain.model.DataTypeKey
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val isLinearLayout: Flow<Boolean>

    val isNightMode: Flow<Boolean>

    val isScreenLocked: Flow<Boolean>

    val isTopBarLocked: Flow<Boolean>

    fun getSearchHistory(dataTypeKey: DataTypeKey): Flow<List<String>>

    suspend fun saveLayoutPreference(isLinearLayout: Boolean)

    suspend fun saveNightModePreference(isNightMode: Boolean)

    suspend fun saveScreenLockedPreference(isScreenLocked: Boolean)

    suspend fun saveTopBarLockedPreference(isTopBarLocked: Boolean)

    suspend fun saveSearchHistory(dataTypeKey: DataTypeKey, searchQuery: String)

    suspend fun cleanAllSearchHistory()

}