package com.wenubey.rickandmortywiki.ui.widget.components


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.unit.ColorProvider
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.widget.CharacterInfo
import com.wenubey.rickandmortywiki.ui.widget.WidgetCallback
import com.wenubey.rickandmortywiki.ui.widget.WidgetTheme
import com.wenubey.rickandmortywiki.ui.widget.getImageProvider
import com.wenubey.rickandmortywiki.utils.shrinkParentheses

@Composable
fun CharacterHorizontalRectangle(
    characterInfo: CharacterInfo,
) {

    WidgetTheme {
        val context = LocalContext.current
        Scaffold(
            modifier = GlanceModifier.fillMaxSize()
                .clickable(actionRunCallback<WidgetCallback>()),
            backgroundColor = GlanceTheme.colors.background,
        ) {
            Row(
                modifier = GlanceModifier.fillMaxSize().padding(4.dp),
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.Vertical.CenterVertically,
            ) {
                Box(modifier = GlanceModifier.defaultWeight()) {
                    Image(
                        modifier = GlanceModifier.padding(4.dp).fillMaxSize(),
                        provider = getImageProvider(characterInfo.characterImageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                    CharacterInfoButton(contentAlignment = Alignment.BottomStart)
                }
                val cardColor = GlanceTheme.colors.surfaceVariant.getColor(context)
                        .copy(alpha = 0.4f)
                Box(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .clickable{
                            Log.i("TAG", "clicked: ")
                        }
                        .background(
                            ImageProvider(R.drawable.corner_16),
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(ColorProvider(cardColor))
                        )
                ) {
                    LazyColumn(
                        modifier = GlanceModifier
                            .defaultWeight()
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        item {
                            CharacterInfoHeader(content = characterInfo.characterName)
                        }
                        item {
                            CharacterInfoRow(
                                headerRes = R.string.character_status,
                                content = characterInfo.characterStatus
                            )
                        }
                        item {
                            Spacer(
                                modifier = GlanceModifier.fillMaxWidth().height(1.dp)
                                    .background(ColorProvider(Color.Magenta))
                            )
                        }
                        item {
                            CharacterInfoRow(
                                headerRes = R.string.character_location,
                                content = characterInfo.characterLocation.shrinkParentheses()
                            )
                        }
                        item {
                            Spacer(
                                modifier = GlanceModifier.fillMaxWidth().height(1.dp)
                                    .background(ColorProvider(Color.Magenta))
                            )
                        }
                        item {
                            CharacterInfoRow(
                                headerRes = R.string.character_species,
                                content = characterInfo.characterSpecies
                            )
                        }
                        item {
                            Spacer(
                                modifier = GlanceModifier.fillMaxWidth().height(1.dp)
                                    .background(ColorProvider(Color.Magenta))
                            )
                        }
                        item {
                            CharacterInfoRow(
                                headerRes = R.string.character_gender,
                                content = characterInfo.characterGender
                            )
                        }
                        item {
                            Spacer(
                                modifier = GlanceModifier.fillMaxWidth().height(1.dp)
                                    .background(ColorProvider(Color.Magenta))
                            )
                        }
                        item {
                            CharacterInfoRow(
                                headerRes = R.string.location_type,
                                content = characterInfo.characterType
                            )
                        }
                    }
                }
            }
        }
    }
}

