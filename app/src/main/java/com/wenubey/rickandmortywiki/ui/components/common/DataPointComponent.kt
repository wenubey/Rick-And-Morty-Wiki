package com.wenubey.rickandmortywiki.ui.components.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wenubey.domain.model.DataPoint
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme


@Composable
fun DataPointComponent(dataPoint: DataPoint) {
    Column(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = dataPoint.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        HorizontalDivider(
            thickness = (0.5).dp,
            color = Color.Magenta.copy(alpha = 0.4f),
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Text(text = dataPoint.description, fontSize = 24.sp)
    }
}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun DataPointComponentPreview() {
    RickAndMortyWikiTheme {
        Surface {
            DataPointComponent(
                dataPoint = DataPoint(
                    title = "Title",
                    description = "Description",
                ),
            )
        }
    }
}
