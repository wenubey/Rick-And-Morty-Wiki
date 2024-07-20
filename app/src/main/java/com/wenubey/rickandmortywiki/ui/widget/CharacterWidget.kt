package com.wenubey.rickandmortywiki.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.widget.components.CharacterHorizontalRectangle
import com.wenubey.rickandmortywiki.ui.widget.components.CharacterSmallSquare

class CharacterWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*>
        get() = PreferencesGlanceStateDefinition

    override val sizeMode: SizeMode =
        SizeMode.Responsive(
            setOf(SMALL_SQUARE, MEDIUM_RECTANGLE, BIG_RECTANGLE),
        )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent { Content() }
    }

    @Composable
    fun Content() {
        val context = LocalContext.current
        val prefs = currentState<Preferences>()
        val size = LocalSize.current
        val characterInfo = getCharacterInfo(prefs, context)

        when (size) {
            SMALL_SQUARE -> CharacterSmallSquare(characterInfo = characterInfo)
            MEDIUM_RECTANGLE -> CharacterSmallSquare(characterInfo = characterInfo)
            BIG_RECTANGLE -> CharacterHorizontalRectangle(characterInfo = characterInfo)
        }
    }

    companion object {
        private val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        private val MEDIUM_RECTANGLE = DpSize(200.dp, 100.dp)
        private val BIG_RECTANGLE = DpSize(300.dp, 100.dp)
    }
}


data class CharacterInfo(
    val characterName: String,
    val characterLocation: String,
    val characterImageUrl: String,
    val characterStatus: String,
    val characterGender: String,
    val characterSpecies: String,
    val characterType: String,
)

private fun getCharacterInfo(prefs: Preferences, context: Context): CharacterInfo {
    val characterImageUrl = prefs[RickAndMortyAppWidgetReceiver.characterImageUrl]
        ?: ""
    val characterName = prefs[RickAndMortyAppWidgetReceiver.characterName]
        ?: context.getString(R.string.error_unknown_field)
    val characterLocation = prefs[RickAndMortyAppWidgetReceiver.characterLocationName]
        ?: context.getString(R.string.error_unknown_field)
    val characterStatus = prefs[RickAndMortyAppWidgetReceiver.characterStatus]
        ?: context.getString(R.string.error_unknown_field)
    val characterGender = prefs[RickAndMortyAppWidgetReceiver.characterGender]
        ?: context.getString(R.string.error_unknown_field)
    val characterSpecies = prefs[RickAndMortyAppWidgetReceiver.characterSpecies]
        ?: context.getString(R.string.error_unknown_field)
    val characterType = prefs[RickAndMortyAppWidgetReceiver.characterType]
        ?: context.getString(R.string.error_unknown_field)


    return CharacterInfo(
        characterName = characterName,
        characterLocation = characterLocation,
        characterImageUrl = characterImageUrl,
        characterStatus = characterStatus,
        characterGender = characterGender,
        characterSpecies = characterSpecies,
        characterType = characterType,
    )
}

fun getImageProvider(path: String): ImageProvider {
    return if (path.isEmpty()) {
        ImageProvider(R.drawable.baseline_404_not_found_24)
    } else {
        val imageLoader = ImageLoader.get().load(path).clip(16f).bitmap!!
        ImageProvider(imageLoader)
    }
}