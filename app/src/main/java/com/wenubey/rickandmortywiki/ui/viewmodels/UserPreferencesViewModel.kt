package com.wenubey.rickandmortywiki.ui.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ScreenLockRotation
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.repositories.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val userPreferencesUiState: StateFlow<UiState> =
        combine(
            userPreferencesRepository.isScreenLocked,
            userPreferencesRepository.isLinearLayout,
            userPreferencesRepository.isNightMode
        ) { isScreenLocked, isLinearLayout, isNightMode ->
            UiState(
                screenLock = ScreenLock(isScreenLocked = isScreenLocked),
                linearLayout = LinearLayout(isLinearLayout = isLinearLayout),
                nightMode = NightMode(isNightMode = isNightMode)
            )
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState()
        )


    fun selectLayout(isLinearLayout: Boolean) = viewModelScope.launch {
        userPreferencesRepository.saveLayoutPreference(isLinearLayout)
    }

    fun selectNightMode(isNightMode: Boolean) = viewModelScope.launch {
        userPreferencesRepository.saveNightModePreference(isNightMode)
    }

    fun selectScreenLock(isScreenLocked: Boolean) = viewModelScope.launch {
        userPreferencesRepository.saveScreenLockedPreference(isScreenLocked)
    }
}


data class UiState(
    val screenLock: ScreenLock = ScreenLock(),
    val nightMode: NightMode = NightMode(),
    val linearLayout: LinearLayout = LinearLayout()
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
        Icons.Filled.LightMode
    } else {
        Icons.Filled.DarkMode
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


