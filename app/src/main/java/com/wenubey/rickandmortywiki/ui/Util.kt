package com.wenubey.rickandmortywiki.ui

import android.content.res.Configuration
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

fun formatSeasonEpisode(seasonNumber: Int, episodeNumber: Int): String {
    val formattedSeason = String.format("%02d", seasonNumber)
    val formattedEpisode = String.format("%02d", episodeNumber)
    return "S" + formattedSeason + "E" + formattedEpisode
}

fun String.parseDate(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = LocalDate.parse(this, inputFormatter)
    return date.format(outputFormatter).toString()
}

@Composable
 fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
fun LazyGridState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}