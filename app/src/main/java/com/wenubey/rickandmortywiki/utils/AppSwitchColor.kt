package com.wenubey.rickandmortywiki.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun appSwitchColors(): SwitchColors {
    return SwitchDefaults.colors(
        checkedIconColor = Color.Magenta,
        uncheckedIconColor = SwitchDefaults.colors().uncheckedBorderColor,
        checkedTrackColor = SwitchDefaults.colors().uncheckedTrackColor,
        checkedThumbColor = MaterialTheme.colorScheme.surface,
        uncheckedThumbColor = MaterialTheme.colorScheme.surface,
        checkedBorderColor = Color.Magenta,
    )
}