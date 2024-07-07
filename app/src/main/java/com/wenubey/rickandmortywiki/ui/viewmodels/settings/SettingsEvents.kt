package com.wenubey.rickandmortywiki.ui.viewmodels.settings

import kotlinx.coroutines.flow.StateFlow

interface SettingsEvents {

    val settingsUiState: StateFlow<SettingsUiState>

    fun selectLayout(isLinearLayout: Boolean)

    fun selectNightMode(isNightMode: Boolean)

    fun selectScreenLock(isScreenLocked: Boolean)

    fun selectTopBarLock(isTopBarLocked: Boolean)

    fun clearAllSearchHistory()
}