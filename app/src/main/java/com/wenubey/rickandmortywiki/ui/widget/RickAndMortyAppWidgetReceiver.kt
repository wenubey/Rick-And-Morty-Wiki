package com.wenubey.rickandmortywiki.ui.widget

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.wenubey.domain.model.Character
import com.wenubey.rickandmortywiki.ui.viewmodels.WidgetUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.WidgetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RickAndMortyAppWidgetReceiver : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var widgetViewModel: WidgetViewModel

    override val glanceAppWidget: GlanceAppWidget
        get() = CharacterWidget()

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        fetchCharacter(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == WidgetCallback.UPDATE_ACTION) {
            fetchCharacter(context)
        }
    }

    private fun fetchCharacter(context: Context) {
        scope.launch {
            widgetViewModel.uiState.collectLatest { state ->
                val glanceId =
                    GlanceAppWidgetManager(context).getGlanceIds(CharacterWidget::class.java)
                        .firstOrNull()

                glanceId?.let { id ->
                    when (state) {
                        is WidgetUiState.Success -> {
                            val character = state.character

                            updateAppWidgetState(
                                context,
                                PreferencesGlanceStateDefinition,
                                id
                            ) { pref ->
                                updatePref(pref, character)
                            }
                            glanceAppWidget.update(context, id)
                        }

                        is WidgetUiState.Loading -> {}
                        is WidgetUiState.Error -> {
                            updateAppWidgetState(context, PreferencesGlanceStateDefinition, id) { pref ->
                                pref.toMutablePreferences().apply {
                                    this[isErrorOccurred] = true
                                }
                            }
                            glanceAppWidget.update(context, id)
                        }
                    }
                }
            }
        }
    }

    private fun updatePref(pref: Preferences, character: Character): MutablePreferences {
       return pref.toMutablePreferences().apply {
            this[characterImageUrl] = character.imageUrl
            this[characterLocationName] = character.location.name
            this[characterName] = character.name
            this[characterStatus] = character.status
            this[characterGender] = character.gender.displayName
            this[characterSpecies] = character.species
            this[characterType] = character.type
            this[isErrorOccurred] = false
        }
    }

    companion object {
        val characterGender = stringPreferencesKey("character_gender")
        val characterImageUrl = stringPreferencesKey("character_image")
        val characterLocationName = stringPreferencesKey("character_location_name")
        val characterName = stringPreferencesKey("character_name")
        val characterSpecies = stringPreferencesKey("character_species")
        val characterStatus = stringPreferencesKey("character_status")
        val characterType = stringPreferencesKey("character_type")
        val isErrorOccurred = booleanPreferencesKey("is_error_occurred")
    }
}