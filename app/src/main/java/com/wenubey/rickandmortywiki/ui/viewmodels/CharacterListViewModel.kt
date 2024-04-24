package com.wenubey.rickandmortywiki.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wenubey.domain.model.Character
import com.wenubey.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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



    init {
        characterPagingFlow()
    }

    private fun characterPagingFlow() = viewModelScope.launch {
        val result = characterRepository
            .getCharacterPage()
            .cachedIn(viewModelScope)
        _characterListUiState.update {
            return@update CharacterListUiState.Success(result)
        }
    }



}


sealed interface CharacterListUiState {
    data object Loading : CharacterListUiState
    data class Error(val message: String) : CharacterListUiState
    data class Success(val charactersFlow: Flow<PagingData<Character>>) : CharacterListUiState
}
