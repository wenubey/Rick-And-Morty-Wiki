package com.wenubey.rickandmortywiki.ui.components.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
                .padding(end = 12.dp),
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

@Composable
fun SettingsItemDivider() {
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp).padding(vertical = 4.dp),thickness = 1.dp, color = Color.Magenta)
}