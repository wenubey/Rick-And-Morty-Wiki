package com.wenubey.rickandmortywiki.ui.components.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


@Composable
fun InfoText(
    @StringRes headerRes: Int,
    @StringRes detailedInfoRes: Int,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(id = headerRes),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(text = stringResource(id = detailedInfoRes), style = MaterialTheme.typography.bodyMedium)
    }


}