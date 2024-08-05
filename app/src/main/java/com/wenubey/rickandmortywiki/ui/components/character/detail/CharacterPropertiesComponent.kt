package com.wenubey.rickandmortywiki.ui.components.character.detail

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Episode
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.utils.formatSeasonEpisode
import com.wenubey.rickandmortywiki.utils.isSystemInPortraitOrientation
import com.wenubey.rickandmortywiki.utils.parseDate
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
        if (isSystemInPortraitOrientation()) {
            Spacer(modifier = Modifier.height(24.dp))
        }
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
        DetailEpisodeProperties(episodes = character.episodes)
    }
}

@Composable
fun DetailEpisodeProperties(
    episodes: List<Episode>,
) {
    var isLoadMoreClicked by remember {
        mutableStateOf(false)
    }
    val listState = rememberLazyListState()
    val groupedEpisodes = episodes.groupBy { it.seasonNumber }
    val lazyListSize = if (episodes.size <= 3) episodes.size * 0.15 else 0.5
    val screenHeight = (LocalConfiguration.current.screenHeightDp * lazyListSize).dp


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight),
        state = listState,
    ) {
        groupedEpisodes.forEach { (season, episodes) ->
            item {
                SeasonHeader(season = season)
            }
            items(episodes) { episode ->
                EpisodeCard(episode = episode, onClick = { isLoadMoreClicked = !isLoadMoreClicked })
            }
        }
    }
}

@Composable
fun SeasonHeader(season: Int) {
    DetailHeaderComponent(
        modifier = Modifier.padding(vertical = 8.dp),
        headerTitle = "${season}. Season", horizontalPadding = 48.dp
    )
}

@Composable
fun EpisodeCard(episode: Episode, onClick: () -> Unit) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
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
            Text(
                text = episode.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal)
            )
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