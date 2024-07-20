package com.wenubey.rickandmortywiki.ui.components.how_to_use

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VideoSectionDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        color = Color.Magenta, modifier = modifier.padding(vertical = 12.dp)
    )
}

