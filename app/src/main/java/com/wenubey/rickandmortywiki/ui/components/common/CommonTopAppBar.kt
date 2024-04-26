package com.wenubey.rickandmortywiki.ui.components.common

import android.content.res.Configuration
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
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    title: String? = null,
    onBackButtonPressed: () -> Unit = {},
    showNavigationIcon: Boolean = true,
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
            UserPreferencesMenu()
        }
    )
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