package com.wenubey.rickandmortywiki.ui.widget.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.layout.Row
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

@Composable
fun CharacterInfoRow(
    @StringRes headerRes: Int,
    content: String
) {
    val context = LocalContext.current
    Row {

        Text(
            modifier = GlanceModifier.padding(horizontal = 4.dp, vertical = 2.dp),
            text = context.getString(headerRes),
            style = TextStyle(
                color = ColorProvider(Color.Magenta),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
        Text(
            modifier = GlanceModifier.padding(horizontal = 4.dp, vertical = 2.dp),
            text = content,
            style = TextStyle(
                color = GlanceTheme.colors.onBackground,
                fontSize = 14.sp
            ),
        )
    }
}