package com.wenubey.rickandmortywiki.ui.viewmodels.user_pref

import kotlinx.coroutines.flow.StateFlow

interface UserPreferencesEvents {

    val userPreferencesUserPrefUiState: StateFlow<UserPrefUiState>

    fun selectLayout(isLinearLayout: Boolean)

    fun selectNightMode(isNightMode: Boolean)

    fun selectScreenLock(isScreenLocked: Boolean)

    fun selectTopBarLock(isTopBarLocked: Boolean)

    fun clearAllSearchHistory()
}