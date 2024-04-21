package com.wenubey.rickandmortywiki.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.wenubey.network.models.domain.Character
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

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
            SubcomposeAsyncImage(
                model = character.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
            CharacterName(
                character = character,
                isDoubleClicked = isDoubleClicked,
            )
        }

    }
}

@Composable
fun CharacterName(
    modifier: Modifier = Modifier,
    character: Character = Character.default(),
    isDoubleClicked: Boolean
) {
    Column(
        modifier = modifier
            .animateContentSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = character.name,
            fontSize = 24.sp,
            maxLines = if (isDoubleClicked) Int.MAX_VALUE else 1,
            overflow = TextOverflow.Clip,
        )
        AnimatedVisibility(visible = isDoubleClicked) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(text = character.species, fontSize = 24.sp)
                Text(text = character.location.name, fontSize = 24.sp)
            }
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