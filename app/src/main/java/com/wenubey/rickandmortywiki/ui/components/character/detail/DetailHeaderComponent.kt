package com.wenubey.rickandmortywiki.ui.components.character.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DetailHeaderComponent(
    modifier: Modifier = Modifier,
    headerTitle: String,
    horizontalPadding: Dp = 32.dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            // TODO change this color when palette created
            color = Color.Magenta,
            modifier = Modifier
                .weight(0.3f)
        )
        Text(
            text = headerTitle,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Magenta,
            modifier = Modifier
                .weight(0.3f)

        )
    }
}