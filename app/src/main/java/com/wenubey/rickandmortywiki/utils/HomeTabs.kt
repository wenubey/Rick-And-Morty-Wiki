package com.wenubey.rickandmortywiki.utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.PeopleAlt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.wenubey.rickandmortywiki.R

enum class HomeTabs(
    @StringRes val text: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    Character(
        text = R.string.tab_characters,
        selectedIcon = Icons.Filled.People,
        unselectedIcon = Icons.Outlined.PeopleAlt
    ),
    Location(
        text = R.string.tabs_locations,
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore,
    ),
    Settings(
        text = R.string.tabs_settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}