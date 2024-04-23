package com.wenubey.rickandmortywiki.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.domain.model.CharacterPage
import com.wenubey.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel
@Inject constructor(
    private val characterRepository: CharacterRepository,
) : ViewModel() {
    private val _characterListUiState = MutableStateFlow<CharacterListUiState>(
        value = CharacterListUiState.Loading
    )
    val characterListUiState: StateFlow<CharacterListUiState> =
        _characterListUiState.asStateFlow()

    private val fetchedCharacterPages = mutableListOf<CharacterPage>()

    fun getInitialPage() = viewModelScope.launch {
        if (fetchedCharacterPages.isNotEmpty()) return@launch
        val initialPage = characterRepository.getCharacterPage(1)
        initialPage
            .onSuccess { characterPage ->
                fetchedCharacterPages.clear()
                fetchedCharacterPages.add(characterPage)

                _characterListUiState.update {
                    Log.w(TAG, "fetchInitialPage:Success: ${characterPage.characters.size}")
                    return@update CharacterListUiState.Success(characters = characterPage.characters)
                }
            }.onFailure { exception ->
                _characterListUiState.update {
                    Log.e(TAG, "fetchInitialPage:Error", exception)
                    return@update CharacterListUiState.Error(
                        exception.localizedMessage ?: UNKNOWN_ERROR
                    )
                }
            }
    }

    fun getNextPage() = viewModelScope.launch {
        val nextPageIndex = fetchedCharacterPages.size + 1
        characterRepository.getCharacterPage(nextPageIndex)
            .onSuccess { characterPage ->
                Log.w(TAG, "getNextPage:Success: ${characterPage.characters.size}")
                fetchedCharacterPages.add(characterPage)
                _characterListUiState.update { currentState ->
                    val currentCharacters =
                        (currentState as? CharacterListUiState.Success)?.characters ?: emptyList()
                    return@update CharacterListUiState.Success(characters = currentCharacters + characterPage.characters)
                }
            }
            .onFailure { exception ->
                _characterListUiState.update {
                    Log.e(TAG, "getNextPage:Error:", exception)
                    return@update CharacterListUiState.Error(
                        message = exception.localizedMessage ?: UNKNOWN_ERROR
                    )
                }
            }
    }

    private companion object {
        const val TAG = "characterListViewModel"
        const val UNKNOWN_ERROR = "Unknown error occurred."
    }
}


sealed interface CharacterListUiState {
    data object Loading : CharacterListUiState
    data class Error(val message: String) : CharacterListUiState
    data class Success(val characters: List<com.wenubey.domain.model.Character> = emptyList()) : CharacterListUiState
}
