package com.wenubey.rickandmortywiki.ui.viewmodels.settings

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ScreenLockRotation
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.ui.graphics.vector.ImageVector
import com.wenubey.rickandmortywiki.R

data class SettingsUiState(
    val screenLock: ScreenLock = ScreenLock(),
    val nightMode: NightMode = NightMode(),
    val linearLayout: LinearLayout = LinearLayout(),
    val topBarLock: TopBarLock = TopBarLock(),
    val characterSearchHistory: CharacterSearchHistory = CharacterSearchHistory(),
    val locationSearchHistory: LocationSearchHistory = LocationSearchHistory(),
)

abstract class ToggleFeature(
    var isEnabled: Boolean = false,
    val enabledIcon: ImageVector,
    val disabledIcon: ImageVector,
    @StringRes val headerContent: Int,
    @StringRes val detailContent: Int,
) {
    val toggleIcon: ImageVector
        get() = if (isEnabled) enabledIcon else disabledIcon

    val contentDescriptionRes: Int
        get() = if (isEnabled) headerContent else detailContent

}

data class CharacterSearchHistory(
    val searchHistory: List<String> = listOf(),
    val toggleIcon: ImageVector = Icons.Filled.History,
    val contentDescriptionRes: Int = R.string.delete_the_search_history_toggle
)

data class LocationSearchHistory(
    val searchHistory: List<String> = listOf(),
    val toggleIcon: ImageVector = Icons.Filled.History,
    val contentDescriptionRes: Int = R.string.delete_the_search_history_toggle
)

data class ScreenLock(
    var isScreenLocked: Boolean = false,
) : ToggleFeature(
    isEnabled = isScreenLocked,
    enabledIcon = Icons.Filled.ScreenLockRotation,
    disabledIcon = Icons.Filled.ScreenRotation,
    headerContent = R.string.screen_locked_header,
    detailContent = R.string.screen_locked_detail,
)

data class NightMode(
    var isNightMode: Boolean = false,
) : ToggleFeature(
    isEnabled = isNightMode,
    enabledIcon = Icons.Filled.DarkMode,
    disabledIcon = Icons.Filled.LightMode,
    headerContent = R.string.night_light_header,
    detailContent = R.string.night_light_detail,
)

data class LinearLayout(
    var isLinearLayout: Boolean = false,
) : ToggleFeature(
    isEnabled = isLinearLayout,
    enabledIcon = Icons.AutoMirrored.Filled.List,
    disabledIcon = Icons.Filled.GridOn,
    headerContent = R.string.grid_list_layout_header,
    detailContent = R.string.grid_list_layout_detail,
)

data class TopBarLock(
    var isTopBarLocked: Boolean = false,
) : ToggleFeature(
    isEnabled = isTopBarLocked,
    enabledIcon = Icons.Outlined.Lock,
    disabledIcon = Icons.Outlined.LockOpen,
    headerContent = R.string.top_bar_header,
    detailContent = R.string.top_bar_detail
)

