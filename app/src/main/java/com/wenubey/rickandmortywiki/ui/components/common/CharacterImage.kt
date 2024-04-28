package com.wenubey.rickandmortywiki.ui.components.common

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

private val defaultModifier = Modifier
    .fillMaxWidth()
    .aspectRatio(1f)
    .clip(RoundedCornerShape(12.dp))

@Composable
fun CharacterImage(
    modifier: Modifier = defaultModifier,
    imageUrl: String = ""
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        modifier = modifier,
        contentDescription = stringResource(R.string.cd_character_image),
        loading = {
            CustomProgressIndicator(modifier = Modifier
                .padding(16.dp)
                .size(25.dp))
        },
        error = {
            Image(
                painter = painterResource(id = R.drawable.baseline_404_not_found_24),
                contentDescription = stringResource(id = R.string.cd_character_image)
            )
        },
    )


}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun CharacterImagePreview() {
    RickAndMortyWikiTheme {
        Surface {
            CharacterImage()
        }
    }
}