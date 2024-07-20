package com.wenubey.rickandmortywiki.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.domain.model.Character
import com.wenubey.domain.repository.CharacterRepository
import com.wenubey.rickandmortywiki.ui.di.IoDispatcher
import com.wenubey.rickandmortywiki.ui.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


class WidgetViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WidgetUiState>(WidgetUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchRandomCharacter()
    }

    private fun fetchRandomCharacter() = viewModelScope.launch(ioDispatcher) {
        val randomId = Random.nextInt(1, 827)
        characterRepository.getCharacter(randomId).onSuccess { character ->
            viewModelScope.launch(mainDispatcher) {
                _uiState.update {
                    WidgetUiState.Success(character)
                }
            }
        }.onFailure { exception ->
            viewModelScope.launch(mainDispatcher) {
                _uiState.update {
                    WidgetUiState.Error(exception.message.toString())
                }
            }
        }
    }

}

sealed interface WidgetUiState {
    data object Loading : WidgetUiState
    data class Error(val message: String) : WidgetUiState
    data class Success(
        val character: Character = Character.default()
    ) : WidgetUiState
}