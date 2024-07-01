package com.wenubey.rickandmortywiki.ui.components.character.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.wenubey.domain.model.Location
import com.wenubey.rickandmortywiki.R

@Composable
fun WhereaboutsDetails(location: Location, onDetailClicked: (String) -> Unit) {

    Column(horizontalAlignment = Alignment.Start) {
        WhereaboutsDetailComponent(
            header = stringResource(R.string.location_name),
            description = location.name,
            onDetailClicked = onDetailClicked,
        )
        WhereaboutsDetailComponent(
            header = stringResource(R.string.location_type),
            description = location.type,
            onDetailClicked = onDetailClicked,
        )
        WhereaboutsDetailComponent(
            header = stringResource(R.string.location_dimension),
            description = location.dimension,
            onDetailClicked = onDetailClicked,
        )
        WhereaboutsDetailComponent(
            header = stringResource(R.string.location_population),
            description = location.residents.size.toString(),
            onDetailClicked = null,
        )
    }
}

@Composable
fun WhereaboutsDetailComponent(
    header: String,
    description: String,
    onDetailClicked: ((String) -> Unit)?,
) {
    val isDescriptionUnknown = description.contentEquals("unknown", ignoreCase = true)

    Row(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = header)
        Text(
            text = description,
            modifier = Modifier
                .clickable {
                    if (!isDescriptionUnknown) {
                        onDetailClicked?.invoke("${header.lowercase()},$description")
                    }
                },
            textDecoration = TextDecoration.Underline
        )
    }
}
