package com.wenubey.rickandmortywiki.ui.components.pref_menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Copyright
import androidx.compose.material.icons.filled.ManageHistory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.viewmodels.user_pref.UserPrefUiState


@Composable
fun UserPreferencesMenu(
    uiState: UserPrefUiState,
    userPreferencesOption: UserPreferencesOption,
    onNightModeToggle: (Boolean) -> Unit,
    onLinearLayoutToggle: (Boolean) -> Unit,
    onScreenLockToggle: (Boolean) -> Unit,
    onTopBarLockToggle: (Boolean) -> Unit,
    clearAllSearchHistory: () -> Unit,
    onCopyRightClicked: () -> Unit,
    onHowToUseClicked: () -> Unit,
) {
    val nightModeState = uiState.nightMode
    val linearLayoutState = uiState.linearLayout
    val screenLockState = uiState.screenLock
    val lockedTopBarState = uiState.topBarLock

    var isExpanded by remember {
        mutableStateOf(false)
    }
    var isNightMode by remember {
        mutableStateOf(nightModeState.isNightMode)
    }
    var isLinearLayout by remember {
        mutableStateOf(linearLayoutState.isLinearLayout)
    }
    var isScreenLocked by remember {
        mutableStateOf(screenLockState.isScreenLocked)
    }
    var isTopBarLocked by remember {
        mutableStateOf(lockedTopBarState.isTopBarLocked)
    }

    Icon(
        modifier = Modifier
            .padding(4.dp)
            .clickable { isExpanded = !isExpanded },
        imageVector = if (isExpanded) Icons.Default.Close else Icons.Filled.Settings,
        contentDescription = stringResource(R.string.cd_dropdown_menu_open_close)
    )
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { isExpanded = false },
        offset = DpOffset((-5).dp, 0.dp)
    ) {

        NightModeMenuItem(
            menuItemNameRes = nightModeState.contentDescriptionRes,
            checked = isNightMode,
            onCheckedChange = {
                isNightMode = !isNightMode
                onNightModeToggle(isNightMode)
            },
            nightModeState = nightModeState,
        )
        CommonMenuItem(
            menuItemNameRes = screenLockState.contentDescriptionRes,
            iconImageVector = screenLockState.toggleIcon,
            contentDescriptionRes = screenLockState.contentDescriptionRes,
            onClick = {
                isScreenLocked = !isScreenLocked
                onScreenLockToggle(isScreenLocked)
            }
        )
        if (userPreferencesOption != UserPreferencesOption.DETAIL) {
            CommonMenuItem(
                menuItemNameRes = linearLayoutState.contentDescriptionRes,
                iconImageVector = linearLayoutState.toggleIcon,
                contentDescriptionRes = linearLayoutState.contentDescriptionRes,
                onClick = {
                    isLinearLayout = !isLinearLayout
                    onLinearLayoutToggle(isLinearLayout)
                }
            )
            CommonMenuItem(
                menuItemNameRes = R.string.clear_all_search_history_toggle,
                iconImageVector = Icons.Filled.ManageHistory,
                contentDescriptionRes = R.string.clear_all_search_history_toggle,
                onClick = clearAllSearchHistory
            )
        }
        CommonMenuItem(
            menuItemNameRes = lockedTopBarState.contentDescriptionRes,
            iconImageVector = lockedTopBarState.toggleIcon,
            contentDescriptionRes = lockedTopBarState.contentDescriptionRes,
            onClick = {
                isTopBarLocked = !isTopBarLocked
                onTopBarLockToggle(isTopBarLocked)
            }
        )
        CommonMenuItem(
            menuItemNameRes = R.string.copyright_section,
            iconImageVector = Icons.Default.Copyright,
            onClick = onCopyRightClicked,
            contentDescriptionRes = R.string.copyright_section,
        )
        CommonMenuItem(
            menuItemNameRes = R.string.how_to_use_section,
            iconImageVector = Icons.AutoMirrored.Filled.Help,
            onClick = onHowToUseClicked,
            contentDescriptionRes = R.string.how_to_use_section
        )
    }
}

enum class UserPreferencesOption {
    DETAIL,
    LIST
}