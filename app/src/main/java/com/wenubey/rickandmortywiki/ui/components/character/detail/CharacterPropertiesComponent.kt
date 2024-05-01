package com.wenubey.rickandmortywiki.ui.components.character.detail

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wenubey.domain.model.Character
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

@Composable
fun CharacterPropertiesComponent(
    character: Character = Character.default(),
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
            origin = character.origin
        )
        DetailPropertiesComponent(
            title = stringResource(R.string.character_location),
            description = character.location.name,
            hasAdditionalData = true,
            location = character.location
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
    val isLoadMoreClicked by remember {
        mutableStateOf(false)
    }

    Column {
        character.episodeIds.take(
            if (isLoadMoreClicked) {
                character.episodeIds.size
            } else {
                3
            }
        ).forEach {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                // TODO when back-end connected change the layout.
                Text(text = "$it.Episode", modifier = Modifier.padding(20.dp))
            }
        }
        OutlinedButton(
            onClick = { /*TODO not yet implemented. load all episodes when clicked */ },
            border = BorderStroke(1.dp, Color.Magenta),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(4.dp)
        ) {
            Text(text = "LOAD MORE")
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