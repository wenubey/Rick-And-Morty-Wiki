package com.wenubey.rickandmortywiki.ui.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ScreenLockRotation
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.data.combine
import com.wenubey.domain.repository.UserPreferencesRepository
import com.wenubey.rickandmortywiki.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val userPreferencesUserPrefUiState: StateFlow<UserPrefUiState> =
        combine(
            userPreferencesRepository.isScreenLocked,
            userPreferencesRepository.isLinearLayout,
            userPreferencesRepository.isNightMode,
            userPreferencesRepository.characterSearchHistory,
            userPreferencesRepository.locationSearchHistory,
            userPreferencesRepository.isTopBarLocked,
            userPreferencesRepository.isSpoilerAlertActive
        ) { isScreenLocked, isLinearLayout, isNightMode, characterSearchHistory, locationSearchHistory, isTopBarLocked, isSpoilerAlertActivated ->
            UserPrefUiState(
                screenLock = ScreenLock(isScreenLocked = isScreenLocked),
                linearLayout = LinearLayout(isLinearLayout = isLinearLayout),
                nightMode = NightMode(isNightMode = isNightMode),
                characterSearchHistory = SearchHistory(searchHistory = characterSearchHistory),
                locationSearchHistory = SearchHistory(searchHistory = locationSearchHistory),
                topBarLock = TopBarLock(isTopBarLocked = isTopBarLocked),
                spoilerAlert = SpoilerAlert(isSpoilerAlertActivated = isSpoilerAlertActivated)
            )
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserPrefUiState()
        )

    private val _lastItemIndex = MutableStateFlow(savedStateHandle[LAST_ITEM_INDEX] ?: 0)
    val lastItemIndex: StateFlow<Int> = _lastItemIndex.asStateFlow()

    fun setLastItemIndex(index: Int) {
        _lastItemIndex.update {
            savedStateHandle[LAST_ITEM_INDEX] = index
            return@update index
        }
    }

    fun selectLayout(isLinearLayout: Boolean) = viewModelScope.launch {
        userPreferencesRepository.saveLayoutPreference(isLinearLayout)
    }

    fun selectNightMode(isNightMode: Boolean) = viewModelScope.launch {
        userPreferencesRepository.saveNightModePreference(isNightMode)
    }

    fun selectScreenLock(isScreenLocked: Boolean) = viewModelScope.launch {
        userPreferencesRepository.saveScreenLockedPreference(isScreenLocked)
    }

    fun selectTopBarLock(isTopBarLocked: Boolean) = viewModelScope.launch {
        userPreferencesRepository.saveTopBarLockedPreference(isTopBarLocked)
    }

    fun selectSpoilerAlertActivation(isSpoilerAlertActivated: Boolean) = viewModelScope.launch {
        userPreferencesRepository.saveSpoilerAlertPreference(isSpoilerAlertActivated)
    }

    fun clearAllSearchHistory() = viewModelScope.launch {
        userPreferencesRepository.cleanAllSearchHistory()
    }

    private companion object {
        const val LAST_ITEM_INDEX = "last_item_index"
    }
}


data class UserPrefUiState(
    val screenLock: ScreenLock = ScreenLock(),
    val nightMode: NightMode = NightMode(),
    val linearLayout: LinearLayout = LinearLayout(),
    val spoilerAlert: SpoilerAlert = SpoilerAlert(),
    val topBarLock: TopBarLock = TopBarLock(),
    val characterSearchHistory: SearchHistory = SearchHistory(),
    val locationSearchHistory: SearchHistory = SearchHistory(),
)

abstract class ToggleFeature(
    var isEnabled: Boolean = false,
    val enabledIcon: ImageVector,
    val disabledIcon: ImageVector,
    val enabledContent: Int,
    val disabledContent: Int,
) {
    val toggleIcon: ImageVector
        get() = if (isEnabled) enabledIcon else disabledIcon

    val contentDescriptionRes: Int
        get() = if (isEnabled) enabledContent else disabledContent

}

data class SearchHistory(
    val searchHistory: List<String> = listOf(),
    val toggleIcon: ImageVector = Icons.Filled.History,
    val contentDescriptionRes: Int = R.string.delete_the_search_history_toggle
)

data class ScreenLock(
    var isScreenLocked: Boolean = false,
) : ToggleFeature(
    isEnabled = isScreenLocked,
    enabledIcon = Icons.Filled.ScreenRotation,
    disabledIcon = Icons.Filled.ScreenLockRotation,
    enabledContent = R.string.screen_locked_toggle,
    disabledContent = R.string.screen_not_locked_toggle,
)

data class NightMode(
    var isNightMode: Boolean = false,
) : ToggleFeature(
    isEnabled = isNightMode,
    enabledIcon = Icons.Filled.DarkMode,
    disabledIcon = Icons.Filled.LightMode,
    enabledContent = R.string.dark_mode_toggle,
    disabledContent = R.string.light_mode_toggle,
)

data class LinearLayout(
    var isLinearLayout: Boolean = false,
) : ToggleFeature(
    isEnabled = isLinearLayout,
    enabledIcon = Icons.Filled.GridOn,
    disabledIcon = Icons.AutoMirrored.Filled.List,
    enabledContent = R.string.grid_layout_toggle,
    disabledContent = R.string.linear_layout_toggle,
)

data class TopBarLock(
    var isTopBarLocked: Boolean = false,
) : ToggleFeature(
    isEnabled = isTopBarLocked,
    enabledIcon = Icons.Outlined.Lock,
    disabledIcon = Icons.Outlined.LockOpen,
    enabledContent = R.string.top_bar_locked_toggle,
    disabledContent = R.string.top_bar_not_locked_toggle
)

data class SpoilerAlert(
    var isSpoilerAlertActivated: Boolean = true,
) : ToggleFeature(
    isEnabled = isSpoilerAlertActivated,
    enabledIcon = Icons.Filled.Visibility,
    disabledIcon = Icons.Filled.VisibilityOff,
    enabledContent = R.string.spoiler_alert_active_toggle,
    disabledContent = R.string.spoiler_alert_not_active_toggle
)


