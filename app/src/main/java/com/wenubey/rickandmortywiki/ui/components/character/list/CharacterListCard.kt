package com.wenubey.rickandmortywiki.ui.components.character.list

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wenubey.domain.model.Character
import com.wenubey.rickandmortywiki.ui.components.common.CharacterImage
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

@Composable
fun CharacterListCard(
    character: Character = Character.default(),
    onCharacterSelected: () -> Unit = {},
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onCharacterSelected,
        // TODO change color when color palette created
        border = BorderStroke(width = 1.dp, color = Color.Magenta)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box {
                CharacterImage(
                    imageUrl = character.imageUrl,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                )
                CharacterStatus(status = character.status)
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = character.name,
                    fontSize = 32.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = character.location.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }

        }
    }
}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun CharacterListCardPreviewPortrait() {
    RickAndMortyWikiTheme {
        Surface {
            CharacterListCard()
        }
    }
}

@Preview(
    name = "Dark mode Landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Preview(
    name = "Light mode Landscape",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
private fun CharacterListCardPreviewLandscape() {
    RickAndMortyWikiTheme {
        Surface {
            Column {
                CharacterListCard()
            }
        }
    }
}