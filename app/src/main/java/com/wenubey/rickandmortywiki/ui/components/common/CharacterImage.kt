package com.wenubey.rickandmortywiki.ui.components.common

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme


@Composable
fun CharacterImage(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .clip(RoundedCornerShape(12.dp)),
    imageUrl: String = ""
) {
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .error(R.drawable.baseline_404_not_found_24)
            .size(Size.ORIGINAL)
            .build(),
        onLoading = { /* TODO add Loading State */ }
    )
    Image(
        modifier = modifier,
        painter = painter, contentDescription = stringResource(R.string.cd_character_image)
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