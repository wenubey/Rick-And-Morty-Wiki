package com.wenubey.rickandmortywiki.ui.viewmodels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wenubey.domain.model.Character
import com.wenubey.domain.repository.CharacterRepository
import com.wenubey.domain.repository.SearchQueryProvider
import com.wenubey.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class CharacterListViewModel
@Inject constructor(
    private val characterRepository: CharacterRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val searchQueryProvider: SearchQueryProvider,
) : ViewModel() {
    private val _characterListUiState = MutableStateFlow<CharacterListUiState>(
        value = CharacterListUiState.Loading
    )
    val characterListUiState: StateFlow<CharacterListUiState> =
        _characterListUiState.asStateFlow()

    private val _searchQuery = MutableStateFlow(searchQueryProvider.getSearchQuery())
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    init {
        characterPagingFlow()
    }

    private fun characterPagingFlow() {
        _searchQuery
            .debounce(500)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                characterRepository.getCharacterPage(query)
            }
            .cachedIn(viewModelScope).also { charactersFlow ->
                _characterListUiState.update {
                    return@update CharacterListUiState.Success(charactersFlow = charactersFlow)
                }
            }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.update {
            return@update query
        }
        searchQueryProvider.setSearchQuery(query)
    }

    fun onActiveChange(active: Boolean) {
        _isSearching.update { return@update active }
    }

    fun onSearch(query: String) {
        setSearchQuery(query)
        onActiveChange(!_isSearching.value)
        saveSearchHistory(query)
    }

    private fun saveSearchHistory(historyItem: String) = viewModelScope.launch {
        userPreferencesRepository.saveSearchHistory(historyItem)
    }

}


sealed interface CharacterListUiState {
    data object Loading : CharacterListUiState
    data class Error(val message: String) : CharacterListUiState
    data class Success(val charactersFlow: Flow<PagingData<Character>>) : CharacterListUiState
}
