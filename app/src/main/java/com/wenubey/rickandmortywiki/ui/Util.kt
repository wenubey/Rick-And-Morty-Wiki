package com.wenubey.rickandmortywiki.ui

import android.content.res.Configuration
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}

fun String.getColorFromCharacterStatus(): Color {
    return when (this.lowercase()) {
        "alive" -> Color.Green
        "dead" -> Color.Red
        else -> Color.Yellow
    }
}

@Composable
fun screenHeight(fraction: Float = 0f): Dp {
    val config = LocalConfiguration.current
    return(config.screenHeightDp * fraction).dp
}

@Composable
fun screenWidth(fraction: Float = 0f): Dp{
    val config = LocalConfiguration.current
    return (config.screenWidthDp * fraction).dp
}