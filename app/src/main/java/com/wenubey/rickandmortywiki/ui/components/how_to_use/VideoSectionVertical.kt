package com.wenubey.rickandmortywiki.ui.components.how_to_use

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.ui.components.common.VideoPlayer

@Composable
fun VideoSectionVertical(
    modifier: Modifier = Modifier,
    @RawRes videoRes: Int,
    @StringRes descriptionRes: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        VideoPlayer(
            modifier = Modifier.size(200.dp, 250.dp),
            videoResource = videoRes
        )
        Text(
            text = stringResource(descriptionRes),
            style = MaterialTheme.typography.bodyMedium.copy(lineBreak = LineBreak.Heading),
        )
    }
}