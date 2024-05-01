package com.wenubey.rickandmortywiki.ui.components.character.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DetailHeaderComponent(
    headerTitle: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            // TODO change this color when palette created
            color = Color.Magenta,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(0.3f)
        )
        Text(text = headerTitle, modifier = Modifier.padding(horizontal = 16.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Magenta,
            modifier = Modifier
                .weight(0.3f)
                .padding(end = 16.dp)

        )
    }
}