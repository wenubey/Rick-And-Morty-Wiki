package com.wenubey.rickandmortywiki.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.components.settings.SettingsItem
import com.wenubey.rickandmortywiki.ui.components.settings.SettingsItemDivider
import com.wenubey.rickandmortywiki.ui.viewmodels.settings.SettingsUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.settings.SettingsEvents

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    events: SettingsEvents,
) {
    val nightModeState = uiState.nightMode
    val linearLayoutState = uiState.linearLayout
    val screenLockState = uiState.screenLock
    val lockedTopBarState = uiState.topBarLock

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp)
            ) {
                SettingsItem(
                    infoTextHeader = nightModeState.headerContent,
                    infoTextDetail = nightModeState.detailContent,
                    switchIconVector = nightModeState.toggleIcon,
                    switchIconContentDescription = nightModeState.contentDescriptionRes,
                    checked = isNightMode,
                    onCheckedChange = {
                        isNightMode = !isNightMode
                        events.selectNightMode(isNightMode)
                    },
                    switchColors = SwitchDefaults.colors(
                        uncheckedTrackColor = colorResource(id = R.color.light_mode_switch),
                        uncheckedBorderColor = colorResource(id = R.color.light_mode_switch),
                        uncheckedThumbColor = colorResource(id = R.color.light_mode_thumb),
                        checkedTrackColor = colorResource(id = R.color.night_mode_switch),
                        checkedBorderColor = colorResource(id = R.color.night_mode_switch),
                        checkedThumbColor = colorResource(id = R.color.night_mode_thumb),
                    )
                )
                SettingsItemDivider()
                SettingsItem(
                    infoTextHeader = lockedTopBarState.headerContent,
                    infoTextDetail = lockedTopBarState.detailContent,
                    switchIconVector = lockedTopBarState.toggleIcon,
                    switchIconContentDescription = lockedTopBarState.contentDescriptionRes,
                    checked = isTopBarLocked,
                    onCheckedChange = {
                        isTopBarLocked = !isTopBarLocked
                        events.selectTopBarLock(isTopBarLocked)
                    }
                )
                SettingsItemDivider()
                SettingsItem(
                    infoTextHeader = linearLayoutState.headerContent,
                    infoTextDetail = linearLayoutState.detailContent,
                    switchIconVector = linearLayoutState.toggleIcon,
                    switchIconContentDescription = linearLayoutState.contentDescriptionRes,
                    checked = isLinearLayout,
                    onCheckedChange = {
                        isLinearLayout = !isLinearLayout
                        events.selectLayout(isLinearLayout)
                    }
                )
                SettingsItemDivider()
                SettingsItem(
                    infoTextHeader = screenLockState.headerContent,
                    infoTextDetail = screenLockState.detailContent,
                    switchIconVector = screenLockState.toggleIcon,
                    switchIconContentDescription = screenLockState.contentDescriptionRes,
                    checked = isScreenLocked,
                    onCheckedChange = {
                        isScreenLocked = !isScreenLocked
                        events.selectScreenLock(isScreenLocked)
                    }
                )
            }
        }
    )
}
