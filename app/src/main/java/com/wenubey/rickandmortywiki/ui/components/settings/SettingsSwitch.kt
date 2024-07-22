package com.wenubey.rickandmortywiki.ui.components.settings

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme
import com.wenubey.rickandmortywiki.utils.appSwitchColors


@Composable
fun SettingsSwitch(
    @StringRes infoTextHeaderRes: Int,
    @StringRes infoTextDetailRes: Int,
    switchIconVector: ImageVector,
    @StringRes switchIconContentDescription: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    switchColors: SwitchColors = appSwitchColors()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InfoText(
            modifier = Modifier.weight(0.8f),
            headerRes =  infoTextHeaderRes,
            detailedInfoRes =  infoTextDetailRes
        )
        Switch(
            modifier = Modifier
                .scale(1.5f)
                .weight(0.2f),
            thumbContent = {
                Icon(
                    modifier = Modifier.padding(1.dp),
                    imageVector = switchIconVector,
                    contentDescription = stringResource(id = switchIconContentDescription)
                )
            },
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = switchColors

        )
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
@Preview(
    name = "Dark mode Portrait",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Preview(
    name = "Light mode Portrait",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Composable
private fun SettingsSwitchPreview() {
     RickAndMortyWikiTheme {
        Surface {
            SettingsSwitch(
                infoTextHeaderRes = R.string.grid_list_layout_header,
                infoTextDetailRes = R.string.grid_list_layout_detail,
                switchIconVector = Icons.AutoMirrored.Filled.List,
                switchIconContentDescription = R.string.grid_list_layout_detail,
                checked = true,
                onCheckedChange = {
                }
            )
        }
    }
}

@Composable
fun SettingsItemDivider() {
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp).padding(vertical = 4.dp),thickness = 1.dp, color = Color.Magenta)
}