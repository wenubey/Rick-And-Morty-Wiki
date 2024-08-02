package com.wenubey.rickandmortywiki.ui.components.character.list

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme
import com.wenubey.domain.model.Character

@Composable
fun CharacterGridNamePlate(
    modifier: Modifier = Modifier,
    character: Character = Character.default(),
    isDoubleClicked: Boolean = false
) {
    Column(
        modifier = modifier
            .animateContentSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = character.name,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = if (isDoubleClicked) Int.MAX_VALUE else 1,
            overflow = TextOverflow.Clip,
        )
        AnimatedVisibility(visible = isDoubleClicked) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(text = character.species, fontSize = 16.sp)
                Text(text = character.gender.displayName, fontSize = 16.sp)
            }
        }
    }
}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun CharacterNamePreview() {
     RickAndMortyWikiTheme {
        Surface {
             CharacterGridNamePlate()
        }
    }
}