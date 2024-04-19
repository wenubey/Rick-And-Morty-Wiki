package com.wenubey.rickandmortywiki.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.network.models.domain.Episode
import com.wenubey.rickandmortywiki.repositories.CharacterRepository
import com.wenubey.rickandmortywiki.repositories.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterEpisodeViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val episodeRepository: EpisodeRepository,
) : ViewModel() {
    private val _characterEpisodeUiState = MutableStateFlow<CharacterEpisodeUiState>(
        value = CharacterEpisodeUiState.Loading
    )
    val characterEpisodeUiState: StateFlow<CharacterEpisodeUiState> =
        _characterEpisodeUiState.asStateFlow()

    fun getEpisodes(characterId: Int) = viewModelScope.launch {
        val episodeIds: MutableList<Int> = mutableListOf()
        characterRepository.getCharacter(characterId)
            .onSuccess { character ->
                Log.w(TAG, "getCharacter:Success: ${character.name}")
                episodeIds.clear()
                episodeIds.addAll(character.episodeIds)
            }
            .onFailure { exception ->
                Log.e(TAG, "getCharacter:Error: ", exception)
                _characterEpisodeUiState.update {
                    return@update CharacterEpisodeUiState.Error(
                        exception.localizedMessage ?: UNKNOWN_ERROR
                    )
                }
            }

        episodeRepository.getEpisodes(episodeIds)
            .onSuccess { episodes ->
                Log.w(TAG, "getEpisodes:Success: ${episodes.size}")
                _characterEpisodeUiState.update {
                    return@update CharacterEpisodeUiState.Success(episodes)
                }
            }
            .onFailure { exception ->
                Log.e(TAG, "getEpisodes:Error: ", exception)
                _characterEpisodeUiState.update {
                    return@update CharacterEpisodeUiState.Error(
                        exception.localizedMessage ?: UNKNOWN_ERROR
                    )
                }
            }

    }

    private companion object {
        const val TAG = "characterEpisodeViewModel"
        const val UNKNOWN_ERROR = "Unknown error occurred."
    }
}

sealed interface CharacterEpisodeUiState {
    data object Loading : CharacterEpisodeUiState
    data class Error(val message: String) : CharacterEpisodeUiState
    data class Success(val episodes: List<Episode> = listOf()) : CharacterEpisodeUiState
}