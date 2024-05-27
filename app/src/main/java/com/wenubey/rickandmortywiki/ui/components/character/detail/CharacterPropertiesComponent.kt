package com.wenubey.rickandmortywiki.ui.components.character.detail

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wenubey.domain.model.Character
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.formatSeasonEpisode
import com.wenubey.rickandmortywiki.ui.parseDate
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

@Composable
fun CharacterPropertiesComponent(
    character: Character = Character.default(),
    onLocationClicked: (String) -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        DetailHeaderComponent(headerTitle = stringResource(R.string.character_properties_header))
        DetailPropertiesComponent(
            title = stringResource(R.string.character_gender),
            description = character.gender.displayName
        )
        DetailPropertiesComponent(
            title = stringResource(R.string.character_species),
            description = character.species
        )
        DetailPropertiesComponent(
            title = stringResource(R.string.character_status),
            description = character.status
        )
        Spacer(modifier = Modifier.height(24.dp))
        DetailHeaderComponent(headerTitle = stringResource(R.string.character_whereabouts_header))
        DetailPropertiesComponent(
            title = stringResource(R.string.character_origin),
            description = character.origin.name,
            hasAdditionalData = true,
            origin = character.origin,
            onLocationClicked = onLocationClicked
        )
        DetailPropertiesComponent(
            title = stringResource(R.string.character_location),
            description = character.location.name,
            hasAdditionalData = true,
            location = character.location,
            onLocationClicked = onLocationClicked
        )
        Spacer(modifier = Modifier.height(24.dp))
        DetailHeaderComponent(headerTitle = stringResource(R.string.character_episodes_header))
        DetailEpisodeProperties(character = character)
    }
}

@Composable
fun DetailEpisodeProperties(
    character: Character,
) {
    var isLoadMoreClicked by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    var episodeSize by remember {
        mutableIntStateOf(character.episodes.size)
    }

    LaunchedEffect(isLoadMoreClicked) {
        episodeSize = if (isLoadMoreClicked) {
            character.episodes.size
        } else {
            3
        }
    }

    val buttonRes = if (isLoadMoreClicked) {
        R.string.episode_list_show_less
    } else {
        R.string.episode_list_show_more
    }

    Column {
        character.episodes.take(episodeSize).forEach { episode ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { isLoadMoreClicked = !isLoadMoreClicked })
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = formatSeasonEpisode(episode.seasonNumber, episode.episodeNumber),
                            modifier = Modifier,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )

                        Text(
                            text = episode.airDate.parseDate(),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Light),
                        )

                    }
                    Text(text = episode.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal))
                }
            }
        }
        OutlinedButton(
            onClick = { isLoadMoreClicked = !isLoadMoreClicked },
            border = BorderStroke(1.dp, Color.Magenta),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(4.dp)
        ) {
            Text(text = stringResource(id = buttonRes), style = MaterialTheme.typography.bodyLarge)
        }
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