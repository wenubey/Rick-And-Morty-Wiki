package com.wenubey.rickandmortywiki.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import com.wenubey.rickandmortywiki.R

class CharacterWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*>
        get() = PreferencesGlanceStateDefinition

    override val sizeMode: SizeMode =
        SizeMode.Responsive(
            setOf(thinMode, smallMode, mediumMode, largeMode),
        )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent { Content() }
    }


    @Composable
    fun Content() {
        val context = LocalContext.current
        val prefs = currentState<Preferences>()
        val characterImageUrl = prefs[RickAndMortyAppWidgetReceiver.characterImageUrl]
            ?: ""
        val characterName = prefs[RickAndMortyAppWidgetReceiver.characterName]
            ?: context.getString(R.string.error_unknown_field)
        val characterLocation = prefs[RickAndMortyAppWidgetReceiver.characterLocation]
            ?: context.getString(R.string.error_unknown_field)

        GlanceTheme {
            Box(
                modifier = GlanceModifier.background(GlanceTheme.colors.background).fillMaxSize()
                    .clickable(actionRunCallback<WidgetCallback>()),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = GlanceModifier.fillMaxSize().padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        provider = getImageProvider(characterImageUrl),
                        contentDescription = null
                    )
                    Text(text = characterName)
                    Text(text = characterLocation)

                }
            }
        }
    }


    companion object {
        private val thinMode = DpSize(120.dp, 120.dp)
        private val smallMode = DpSize(184.dp, 184.dp)
        private val mediumMode = DpSize(260.dp, 200.dp)
        private val largeMode = DpSize(260.dp, 280.dp)
    }

}

private fun getImageProvider(path: String): ImageProvider {
    return if (path.isEmpty()) {
        ImageProvider(R.drawable.baseline_404_not_found_24)
    } else {
        val imageLoader = ImageLoader.get().load(path).bitmap()!!
        ImageProvider(imageLoader)
    }
}