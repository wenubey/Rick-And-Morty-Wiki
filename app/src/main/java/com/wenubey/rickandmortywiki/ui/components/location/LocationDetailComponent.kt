package com.wenubey.rickandmortywiki.ui.components.location

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wenubey.domain.model.Location
import com.wenubey.rickandmortywiki.R

@Composable
fun LocationDetailComponent(
    modifier: Modifier = Modifier,
    location: Location = Location.default()
) {
    OutlinedCard(
        modifier = modifier
            .padding(4.dp),
        border = BorderStroke(width = 1.dp, color = Color.Magenta),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                textAlign = TextAlign.Center,
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LocationDetailText(text = location.dimension)
                LocationDetailText(text = location.type)
                LocationDetailText(
                    text = stringResource(
                        R.string.location_detail_population,
                        location.residents.size
                    ),
                )
            }
        }

    }
}

@Composable
private fun LocationDetailText(text: String) {
    Text(
        text = text,
        fontSize = 28.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
    )
}