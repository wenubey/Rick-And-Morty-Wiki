package com.wenubey.rickandmortywiki.ui.components.pref_menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPreferencesViewModel

// TODO should do state hoisting cuz it's not testable and can't see the previews.
@Composable
fun UserPreferencesMenu(
    viewModel: UserPreferencesViewModel = hiltViewModel(),
) {
    val uiState = viewModel.userPreferencesUserPrefUiState.collectAsState().value
    val nightModeState = uiState.nightMode
    val linearLayoutState = uiState.linearLayout
    val screenLockState = uiState.screenLock

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
            checked = isNightMode,
            onCheckedChange = {
                isNightMode = !isNightMode
                viewModel.selectNightMode(isNightMode)
            },
            nightModeState = nightModeState,
        )
        CommonMenuItem(
            menuItemNameRes = R.string.menu_screen_lock,
            iconImageVector = screenLockState.toggleIcon,
            contentDescriptionRes = screenLockState.contentDescriptionRes,
            onClick = {
                isScreenLocked = !isScreenLocked
                viewModel.selectScreenLock(isScreenLocked)
            }
        )
        CommonMenuItem(
            menuItemNameRes = R.string.menu_linear_layout,
            iconImageVector = linearLayoutState.toggleIcon,
            contentDescriptionRes = linearLayoutState.contentDescriptionRes,
            onClick = {
                isLinearLayout = !isLinearLayout
                viewModel.selectLayout(isLinearLayout)
            }
        )
    }
}