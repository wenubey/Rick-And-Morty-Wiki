package com.wenubey.rickandmortywiki.ui.components.common

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

private val defaultModifier = Modifier
    .padding(128.dp)
    .fillMaxSize()
private val defaultSize = 128.dp


@Composable
fun CustomProgressIndicator(
    modifier: Modifier = defaultModifier,
    indicatorSize: Dp = defaultSize,
) {
    val infiniteTransition = rememberInfiniteTransition(label = stringResource(R.string.label_loading_indicator_animation))
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
        ), label = stringResource(R.string.label_loading_indicator_angle)
    )
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier.size(indicatorSize).rotate(angle),
            painter = painterResource(id = R.drawable.loading_icon),
            contentDescription = stringResource(R.string.cd_loading),
        )
    }

}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun CustomProgressIndicatorPreview() {
    RickAndMortyWikiTheme {
        Surface {
            CustomProgressIndicator()
        }
    }
}