package com.wenubey.rickandmortywiki.ui.components.character.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.wenubey.rickandmortywiki.ui.components.common.CharacterImage
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

@Composable
fun CharacterStatusDetailComponent(characterStatus: String, imageUrl: String) {
    val localDensity = LocalDensity.current
    var boxSize by remember { mutableStateOf<DpSize?>(null) }

    val sizeModifier = if (boxSize != null) {
        Modifier.size(boxSize!! + DpSize(40.dp, 40.dp))
    } else {
        Modifier
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Box(
            modifier = sizeModifier.padding(bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.85f)
                    .aspectRatio(1f)
                    .onGloballyPositioned { coordinates ->
                        boxSize = with(localDensity) {
                            coordinates.size
                                .toSize()
                                .toDpSize()
                        }
                    }
                    .border(
                        width = 6.dp,
                        color = Color(0xFF58CF8E),
                        shape = CircleShape
                    )
            ) {
                CharacterImage(
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize()


                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFF58CF8E))
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    text = characterStatus.uppercase(),
                    fontSize = 24.sp,
                )
            }
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
