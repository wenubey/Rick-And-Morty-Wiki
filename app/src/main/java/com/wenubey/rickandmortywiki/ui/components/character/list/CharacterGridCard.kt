package com.wenubey.rickandmortywiki.ui.components.character.list

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.ui.components.common.CharacterImage
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme
import com.wenubey.domain.model.Character

@Composable
fun CharacterGridCard(
    character: Character = Character.default(),
    onCardClick: () -> Unit = {},
) {
    var isDoubleClicked by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        isDoubleClicked = !isDoubleClicked
                    },
                    onTap = { onCardClick() },
                )
            },
        border = BorderStroke(width = 1.dp, color = Color.Magenta)
    ) {
        Column {
            Box {
                CharacterImage(imageUrl = character.imageUrl)
                CharacterStatus(status = character.status)
            }
            CharacterGridNamePlate(
                character = character,
                isDoubleClicked = isDoubleClicked,
            )
        }

    }
}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun CharacterGridCardPreview() {
    RickAndMortyWikiTheme {
        Surface {
            CharacterGridCard()
        }
    }
}