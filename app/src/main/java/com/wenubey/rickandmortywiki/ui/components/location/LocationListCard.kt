package com.wenubey.rickandmortywiki.ui.components.location

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wenubey.domain.model.Location
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

@Composable
fun LocationListCard(
    location: Location = Location.default(),
    onLocationSelected: () -> Unit = {},
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onLocationSelected,
        // TODO change color when color palette created
        border = BorderStroke(width = 1.dp, color = Color.Magenta)
    ) {

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = location.name, fontWeight = FontWeight.Bold, fontSize = 24.sp, textAlign = TextAlign.Center)
                Text(text = location.type, fontSize = 16.sp, textAlign = TextAlign.Center)
                Text(text = location.dimension, fontSize = 16.sp, textAlign = TextAlign.Center)
                Text(text = "Population: ${location.residents.size}", fontSize = 16.sp, textAlign = TextAlign.Center)
            }

    }
}


@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun LocationListCardPreview() {
    RickAndMortyWikiTheme {
        Surface {
            LocationListCard()
        }
    }
}