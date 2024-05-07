package com.wenubey.rickandmortywiki.ui.components.character.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.ui.components.common.CharacterImage
import com.wenubey.rickandmortywiki.ui.components.common.GlowingCard
import com.wenubey.rickandmortywiki.ui.getColorFromCharacterStatus
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

@Composable
fun CharacterStatusDetailComponent(characterStatus: String, imageUrl: String) {
    val characterStatusColor = characterStatus.getColorFromCharacterStatus()

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        GlowingCard(
            modifier = Modifier
                .size(300.dp)
                .padding(8.dp),
            glowingColor = characterStatusColor,
            cornersRadius = 32.dp,
            statusContent = characterStatus,
            glowingRadius = 75.dp
        ) {
            CharacterImage(
                imageUrl = imageUrl,
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .fillMaxSize()


            )
        }

    }
}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun CharacterStatusDetailComponentPreview() {
    RickAndMortyWikiTheme {
        Surface {
            CharacterStatusDetailComponent(characterStatus = "Alive", imageUrl = "url")
        }
    }
}
