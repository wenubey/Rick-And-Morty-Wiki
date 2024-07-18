package com.wenubey.rickandmortywiki.ui.widget.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.unit.ColorProvider
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.MainActivity
import com.wenubey.rickandmortywiki.ui.widget.WidgetTheme

@Composable
fun CharacterInfoButton(
    contentAlignment: Alignment
) {
    WidgetTheme {
        Box(modifier = GlanceModifier.fillMaxSize(), contentAlignment = contentAlignment) {
            CircleIconButton(
                imageProvider = ImageProvider(R.drawable.baseline_info_24),
                contentDescription = null,
                onClick = actionStartActivity<MainActivity>(),
                backgroundColor = GlanceTheme.colors.surfaceVariant,
                contentColor = ColorProvider(Color.Magenta)

            )
        }

    }
}