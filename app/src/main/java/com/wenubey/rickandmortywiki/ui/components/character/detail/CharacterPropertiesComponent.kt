package com.wenubey.rickandmortywiki.ui.components.character.detail

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Location
import com.wenubey.domain.model.Origin
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

@Composable
fun CharacterPropertiesComponent(
    character: Character = Character.default(),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        DetailHeaderComponent(headerTitle = "PROPERTIES")
        DetailPropertiesComponent(title = "Gender", description = character.gender.displayName)
        DetailPropertiesComponent(title = "Species", description = character.species)
        DetailPropertiesComponent(title = "Status", description = character.status)
        DetailHeaderComponent(headerTitle = "WHEREABOUTS")
        DetailPropertiesComponent(title = "Origin", description = character.origin.name, hasAdditionalData = true , origin = character.origin)
        DetailPropertiesComponent(title = "Location", description = character.location.name, hasAdditionalData = true, location = character.location)
    }
}

@Composable
fun DetailPropertiesComponent(
    title: String,
    description: String,
    hasAdditionalData: Boolean = false,
    location: Location? = null,
    origin: Origin? = null,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Card(
                modifier = Modifier.weight(0.3f),

                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = title)
                }

            }
            Card(
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { expanded = !expanded },
                    )
                    .weight(0.7f),
                colors = CardDefaults.cardColors(
                    containerColor = CardDefaults.cardColors().containerColor.copy(alpha = 0.4f)
                ),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = description,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        if (hasAdditionalData) {
                            Icon(
                                imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 4.dp)

                            )
                        }
                    }
                }

            }
        }
        if (expanded) {
            Card(modifier = Modifier.fillMaxWidth()) {
                if (location != null) {
                    Text(text = location.name)
                    Text(text = location.dimension)
                    location.locationResidents?.forEach {
                        Text(text = it.name)
                        Text(text = it.species)
                    }
                }
                if (origin != null) {
                    Text(text = origin.name)
                    Text(text = origin.dimension)
                    origin.originResidents?.forEach {
                        Text(text = it.name)
                        Text(text = it.species)
                    }
                }
            }

        }
    }
}

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

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun CharacterPropertiesComponentPreview() {
    RickAndMortyWikiTheme {
        Surface {
            CharacterPropertiesComponent(

            )
        }
    }
}