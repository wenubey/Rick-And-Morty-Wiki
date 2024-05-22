package com.wenubey.rickandmortywiki.ui.components.common

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.components.pref_menu.UserPreferencesMenu
import com.wenubey.rickandmortywiki.ui.components.pref_menu.UserPreferencesOption
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPrefUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    isVisible: Boolean = true,
    isCopyRightClicked: () -> Unit = {},
    title: String? = null,
    onBackButtonPressed: () -> Unit = {},
    showNavigationIcon: Boolean = true,
    onNightModeToggle: (Boolean) -> Unit = {},
    onLinearLayoutToggle: (Boolean) -> Unit = {},
    onScreenLockToggle: (Boolean) -> Unit = {},
    onTopBarLockToggle: (Boolean) -> Unit = {},
    clearAllSearchHistory: () -> Unit = {},
    uiState: UserPrefUiState = UserPrefUiState(),
    userPreferencesOption: UserPreferencesOption = UserPreferencesOption.LIST,
) {
    AnimatedVisibility(
        visible = isVisible,
        exit = slideOutVertically(),
        enter = slideInVertically(),
    ) {
        TopAppBar(
            title = { Text(text = title ?: stringResource(id = R.string.app_name)) },
            navigationIcon = {
                if (showNavigationIcon) {
                    IconButton(onClick = onBackButtonPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back),
                        )
                    }
                }
            },
            actions = {
                UserPreferencesMenu(
                    onNightModeToggle = onNightModeToggle,
                    onLinearLayoutToggle = onLinearLayoutToggle,
                    onScreenLockToggle = onScreenLockToggle,
                    onTopBarLockToggle = onTopBarLockToggle,
                    clearAllSearchHistory = clearAllSearchHistory,
                    uiState = uiState,
                    userPreferencesOption = userPreferencesOption,
                    isCopyRightClicked = isCopyRightClicked,
                )
            }
        )
    }

}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun CommonTopAppBarPreview() {
    RickAndMortyWikiTheme {
        Surface {
            CommonTopAppBar()
        }
    }
}