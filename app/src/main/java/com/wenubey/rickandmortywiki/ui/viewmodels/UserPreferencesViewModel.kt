package com.wenubey.rickandmortywiki.ui.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ScreenLockRotation
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.domain.repository.UserPreferencesRepository
import com.wenubey.rickandmortywiki.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val userPreferencesUiState: StateFlow<UiState> =
        combine(
            userPreferencesRepository.isScreenLocked,
            userPreferencesRepository.isLinearLayout,
            userPreferencesRepository.isNightMode,
            userPreferencesRepository.searchHistory
        ) { isScreenLocked, isLinearLayout, isNightMode, searchHistory ->
            UiState(
                screenLock = ScreenLock(isScreenLocked = isScreenLocked),
                linearLayout = LinearLayout(isLinearLayout = isLinearLayout),
                nightMode = NightMode(isNightMode = isNightMode),
                searchHistory = SearchHistory(searchHistory = searchHistory)
            )
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState()
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

    fun saveSearchHistory(searchQuery: String) = viewModelScope.launch {
        userPreferencesRepository.saveSearchHistory(searchQuery)
    }

    private companion object {
        const val LAST_ITEM_INDEX = "last_item_index"
    }
}


data class UiState(
    val screenLock: ScreenLock = ScreenLock(),
    val nightMode: NightMode = NightMode(),
    val linearLayout: LinearLayout = LinearLayout(),
    val searchHistory: SearchHistory = SearchHistory(),
)

data class SearchHistory(
    val searchHistory: List<String> = listOf(),
    val toggleIcon: ImageVector = Icons.Filled.History,
    val contentDescriptionRes: Int = R.string.delete_the_search_history_toggle
)

data class ScreenLock(
    var isScreenLocked: Boolean = false,
    val toggleIcon: ImageVector = if (isScreenLocked) {
        Icons.Filled.ScreenRotation
    } else {
        Icons.Filled.ScreenLockRotation
    },
    val contentDescriptionRes: Int = if (isScreenLocked) {
        R.string.screen_locked_toggle
    } else {
        R.string.screen_not_locked_toggle
    }
)
data class NightMode(
    var isNightMode: Boolean = false,
    val toggleIcon: ImageVector = if (isNightMode) {
        Icons.Filled.DarkMode
    } else {
        Icons.Filled.LightMode
    },
    val contentDescriptionRes: Int = if (isNightMode) {
        R.string.light_mode_toggle
    } else {
        R.string.dark_mode_toggle
    }
)
data class LinearLayout(
    var isLinearLayout: Boolean = true,
    val toggleIcon: ImageVector = if (isLinearLayout) {
        Icons.Filled.GridOn
    } else {
        Icons.AutoMirrored.Filled.List
    },
    val contentDescriptionRes: Int = if (isLinearLayout) {
        R.string.grid_layout_toggle
    } else {
        R.string.linear_layout_toggle
    }
)


