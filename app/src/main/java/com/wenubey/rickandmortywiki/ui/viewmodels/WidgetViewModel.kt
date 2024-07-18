package com.wenubey.rickandmortywiki.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.domain.model.Character
import com.wenubey.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


class WidgetViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WidgetUiState>(WidgetUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchRandomCharacter()
    }

    fun fetchRandomCharacter() = viewModelScope.launch {
        val randomId = Random.nextInt(1, 827)
        characterRepository.getCharacter(randomId).onSuccess {
            _uiState.value = WidgetUiState.Success(it)
        }.onFailure {
            _uiState.value = WidgetUiState.Error(it.message.toString())
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