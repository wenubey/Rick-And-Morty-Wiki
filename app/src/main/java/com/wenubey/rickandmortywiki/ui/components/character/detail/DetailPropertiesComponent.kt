package com.wenubey.rickandmortywiki.ui.components.character.detail

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wenubey.domain.model.Location
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

@Composable
fun DetailPropertiesComponent(
    title: String,
    description: String,
    hasAdditionalData: Boolean = false,
    location: Location? = null,
    origin: Location? = null,
    onLocationClicked: (String) -> Unit = {},
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(0.3f),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold))
            }
        }
        DetailPropertiesDescription(
            hasAdditionalData = hasAdditionalData,
            modifier = Modifier.weight(0.7f),
            expanded = expanded,
            description = description,
            onDescriptionClicked = {
                expanded = !expanded
            }
        )
    }
    if (expanded) {
        Card(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            onClick = {
                expanded = !expanded
            },

            ) {
            origin?.let {
                WhereaboutsDetails(
                    location = it,
                    onDetailClicked = onLocationClicked,
                )
            }
            location?.let {
                WhereaboutsDetails(
                    location = it,
                    onDetailClicked = onLocationClicked,
                )
            }

        }

    }
}

@Composable
fun DetailPropertiesDescription(
    modifier: Modifier = Modifier,
    hasAdditionalData: Boolean,
    onDescriptionClicked: () -> Unit,
    expanded: Boolean,
    description: String,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isDescriptionBlankOrEmpty = description.isEmpty() || description.isBlank()
    Card(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (hasAdditionalData && !isDescriptionBlankOrEmpty) {
                        onDescriptionClicked()
                    }
                },
            ),
        colors = CardDefaults.cardColors(
            containerColor = CardDefaults.cardColors().containerColor.copy(alpha = 0.4f)
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = if (isDescriptionBlankOrEmpty) stringResource(id = R.string.error_unknown_field) else description,
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Normal)
                )
                if (hasAdditionalData && !isDescriptionBlankOrEmpty) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp),
                    )
                }
            }
        }

    }
}


@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun DetailPropertiesComponentPreview() {
    RickAndMortyWikiTheme {
        Surface {
            DetailPropertiesComponent(
                title = "Title",
                description = "Post-Apocalyptic Earth",
                hasAdditionalData = true,
            )
        }
    }
}