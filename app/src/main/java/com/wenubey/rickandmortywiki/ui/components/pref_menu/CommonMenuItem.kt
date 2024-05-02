package com.wenubey.rickandmortywiki.ui.components.pref_menu

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

@Composable
fun CommonMenuItem(
    iconImageVector: ImageVector = Icons.Default.DeveloperMode,
    @StringRes menuItemNameRes: Int = R.string.app_name,
    @StringRes contentDescriptionRes: Int = R.string.app_name,
    onClick: () -> Unit = {},
) {
    DropdownMenuItem(
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(end = 8.dp), text = stringResource(menuItemNameRes))
                Icon(
                    imageVector = iconImageVector,
                    contentDescription = stringResource(
                        id = contentDescriptionRes
                    )
                )
            }
        },
        onClick = onClick,
    )
}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun CommonMenuItemPreview() {
    RickAndMortyWikiTheme {
        Surface {
            CommonMenuItem()
        }
    }
}