package com.wenubey.rickandmortywiki.ui.components.pref_menu

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.NoRippleTheme
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme
import com.wenubey.rickandmortywiki.ui.viewmodels.NightMode


@Composable
fun NightModeMenuItem(
    @StringRes menuItemNameRes: Int = R.string.app_name,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    nightModeState: NightMode = NightMode(),
) {
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        DropdownMenuItem(
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Text(text = stringResource(menuItemNameRes))

                    NightModeSwitch(
                        nightModeState = nightModeState,
                        onCheckedChange = onCheckedChange,
                        checked = checked
                    )
                }
            },
            onClick = {},
        )
    }
}

@Composable
fun NightModeSwitch(
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    nightModeState: NightMode = NightMode()
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        thumbContent = {
            Icon(
                imageVector = nightModeState.toggleIcon,
                contentDescription = stringResource(
                    id = nightModeState.contentDescriptionRes
                )
            )
        },
        colors = SwitchDefaults.colors(
            uncheckedTrackColor = colorResource(id = R.color.light_mode_switch),
            uncheckedBorderColor = colorResource(id = R.color.light_mode_switch),
            uncheckedThumbColor = colorResource(id = R.color.light_mode_thumb),
            checkedTrackColor =colorResource(id = R.color.night_mode_switch),
            checkedBorderColor = colorResource(id = R.color.night_mode_switch),
            checkedThumbColor = colorResource(id = R.color.night_mode_thumb),
        )
    )
}



@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun NightModeSwitchPreview() {
    RickAndMortyWikiTheme {
        Surface {
            NightModeSwitch()
        }
    }
}