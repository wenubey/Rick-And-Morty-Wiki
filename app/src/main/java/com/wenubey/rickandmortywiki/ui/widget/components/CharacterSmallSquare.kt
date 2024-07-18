package com.wenubey.rickandmortywiki.ui.widget.components

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.widget.CharacterInfo
import com.wenubey.rickandmortywiki.ui.widget.WidgetCallback
import com.wenubey.rickandmortywiki.ui.widget.WidgetTheme
import com.wenubey.rickandmortywiki.ui.widget.getImageProvider

@Composable
fun CharacterSmallSquare(
    characterInfo: CharacterInfo,
) {
    WidgetTheme {
        Column(
            modifier = GlanceModifier
                .background(ImageProvider(R.drawable.corner_16))
                .fillMaxSize()
        ) {
            Image(
                modifier = GlanceModifier.fillMaxSize().clickable(actionRunCallback<WidgetCallback>()),
                provider = getImageProvider(characterInfo.characterImageUrl),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}