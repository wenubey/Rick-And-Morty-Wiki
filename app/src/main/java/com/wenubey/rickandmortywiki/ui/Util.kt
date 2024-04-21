package com.wenubey.rickandmortywiki.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun isSystemInPortraitOrientation(): Boolean {
    val config = LocalConfiguration.current
    val isSystemInPortrait by remember {
        mutableStateOf(
            when (config.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> false
                else -> true
            }
        )
    }
    return isSystemInPortrait
}