package com.wenubey.rickandmortywiki.ui.components.common

import android.content.res.Configuration
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

@Composable
fun ScrollToTopFAB(
    onClick: () -> Unit
) {
    // TODO change color when palette created.
    FloatingActionButton(onClick = onClick, containerColor = Color.Magenta, shape = RoundedCornerShape(32.dp)) {
        Icon(
            imageVector = Icons.Filled.ArrowUpward,
            contentDescription = stringResource(R.string.cd_scroll_to_top),
        )
    }
}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun ScrollToTopFABPreview() {
    RickAndMortyWikiTheme {
        Surface {
            ScrollToTopFAB(
                onClick = {}
            )
        }
    }
}