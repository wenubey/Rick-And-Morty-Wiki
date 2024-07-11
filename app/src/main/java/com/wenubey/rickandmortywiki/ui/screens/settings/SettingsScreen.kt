package com.wenubey.rickandmortywiki.ui.screens.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.components.settings.InfoText
import com.wenubey.rickandmortywiki.ui.components.settings.SettingsItemDivider
import com.wenubey.rickandmortywiki.ui.components.settings.SettingsSwitch
import com.wenubey.rickandmortywiki.ui.viewmodels.settings.SettingsEvents
import com.wenubey.rickandmortywiki.ui.viewmodels.settings.SettingsUiState

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    events: SettingsEvents,
    onNavigateToCopyrightScreen: () -> Unit,
    onNavigateToHowToUseScreen: () -> Unit,
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

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(8.dp)
            ) {
                SettingsSwitch(
                    infoTextHeaderRes = nightModeState.headerContent,
                    infoTextDetailRes = nightModeState.detailContent,
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
                SettingsSwitch(
                    infoTextHeaderRes = lockedTopBarState.headerContent,
                    infoTextDetailRes = lockedTopBarState.detailContent,
                    switchIconVector = lockedTopBarState.toggleIcon,
                    switchIconContentDescription = lockedTopBarState.contentDescriptionRes,
                    checked = isTopBarLocked,
                    onCheckedChange = {
                        isTopBarLocked = !isTopBarLocked
                        events.selectTopBarLock(isTopBarLocked)
                    }
                )
                SettingsItemDivider()
                SettingsSwitch(
                    infoTextHeaderRes = linearLayoutState.headerContent,
                    infoTextDetailRes = linearLayoutState.detailContent,
                    switchIconVector = linearLayoutState.toggleIcon,
                    switchIconContentDescription = linearLayoutState.contentDescriptionRes,
                    checked = isLinearLayout,
                    onCheckedChange = {
                        isLinearLayout = !isLinearLayout
                        events.selectLayout(isLinearLayout)
                    }
                )
                SettingsItemDivider()
                SettingsSwitch(
                    infoTextHeaderRes = screenLockState.headerContent,
                    infoTextDetailRes = screenLockState.detailContent,
                    switchIconVector = screenLockState.toggleIcon,
                    switchIconContentDescription = screenLockState.contentDescriptionRes,
                    checked = isScreenLocked,
                    onCheckedChange = {
                        isScreenLocked = !isScreenLocked
                        events.selectScreenLock(isScreenLocked)
                    }
                )
                SettingsItemDivider()
                SettingsSection(
                    headerRes = R.string.copyright_header,
                    detailRes = R.string.copyright_detail,
                    onNavigateTo = onNavigateToCopyrightScreen
                )
                SettingsItemDivider()
                SettingsSection(
                    headerRes = R.string.how_to_use_header,
                    detailRes = R.string.how_to_use_detail,
                    onNavigateTo = onNavigateToHowToUseScreen
                )
            }
        }
    )
}

@Composable
fun SettingsSection(
    @StringRes headerRes: Int,
    @StringRes detailRes: Int,
    navigationVector: ImageVector = Icons.AutoMirrored.Filled.ArrowForward,
    onNavigateTo: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                onClick = onNavigateTo,
                indication = null
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfoText(
            modifier = Modifier.weight(0.7f),
            headerRes = headerRes,
            detailedInfoRes = detailRes
        )
        Icon(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(40.dp),
            imageVector = navigationVector,
            contentDescription = stringResource(id = headerRes),
            tint = Color.Magenta
        )
    }
}
