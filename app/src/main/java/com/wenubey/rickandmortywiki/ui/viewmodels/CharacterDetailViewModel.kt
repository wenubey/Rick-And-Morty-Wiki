package com.wenubey.rickandmortywiki.ui.viewmodels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.DataPoint
import com.wenubey.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/* TODO connect to the UI
 * TODO create UI components for detail
 * TODO don't forget the screen compatibility
 * TODO don't forget the handle config changes like screen orientation change
 */
@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _characterDetailUiState = MutableStateFlow<CharacterDetailUiState>(
        value = CharacterDetailUiState.Loading
    )
    val characterDetailUiState: StateFlow<CharacterDetailUiState> =
        _characterDetailUiState.asStateFlow()


    fun getCharacter(id: Int) = viewModelScope.launch {
        _characterDetailUiState.update { return@update CharacterDetailUiState.Loading }
        characterRepository.getCharacter(id)
            .onSuccess { character ->
                Log.w(TAG, "getCharacter:Success: ${character.name}")
                val dataPoints = createDataPoints(character)
                _characterDetailUiState.update {
                    return@update CharacterDetailUiState.Success(
                        character = character,
                        dataPoints = dataPoints
                    )
                }
            }
            .onFailure { exception ->
                Log.e(TAG, "getCharacter:Error", exception)
                _characterDetailUiState.update {
                    return@update CharacterDetailUiState.Error(
                        message = exception.localizedMessage ?: UNKNOWN_ERROR
                    )
                }
            }
    }

    private fun createDataPoints(character: Character): List<DataPoint> {
        return buildList {
            add(DataPoint(title = "Last known location", description = character.location.name))
            add(DataPoint(title = "Species", description = character.species))
            add(DataPoint(title = "Gender", description = character.gender.displayName))
            add(DataPoint(title = "Origin", description = character.origin.name))
            add(DataPoint(title = "Episode count", description = character.episodeIds.size.toString()))
            character.type.takeIf { it.isNotEmpty() }?.let { type ->
                add(DataPoint(title = "Type", description = type))
            }
        }
    }

    private companion object {
        const val TAG = "characterDetailViewModel"
        const val UNKNOWN_ERROR = "Unknown error occurred."
    }
}

sealed interface CharacterDetailUiState {
    data object Loading : CharacterDetailUiState
    data class Error(val message: String) : CharacterDetailUiState
    data class Success(
        val character: Character = Character.default(),
        val dataPoints: List<DataPoint> = listOf()
    ) : CharacterDetailUiState
}