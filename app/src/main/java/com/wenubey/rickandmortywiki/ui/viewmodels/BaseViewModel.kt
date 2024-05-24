package com.wenubey.rickandmortywiki.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.SearchQueryProvider
import com.wenubey.domain.repository.UserPreferencesRepository
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


@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
abstract class BaseViewModel<T : Any> (
    private val searchQueryProvider: SearchQueryProvider,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val savedStateHandle: SavedStateHandle,
    private val dataTypeKey: DataTypeKey,
) : ViewModel(), ListScreenEvents {

    private val _uiState = MutableStateFlow<ListScreenUiState<T>>(ListScreenUiState.Loading())
    val uiState: StateFlow<ListScreenUiState<T>> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow(searchQueryProvider.getSearchQuery(dataTypeKey))
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _lastItemIndex =
        MutableStateFlow(savedStateHandle["${dataTypeKey}_last_item_index"] ?: 0)
    val lastItemIndex: StateFlow<Int> = _lastItemIndex.asStateFlow()

    init {
        pagingFlow()
    }

    abstract fun getPage(): Flow<PagingData<T>>

    private fun pagingFlow() {
        _searchQuery
            .debounce(500)
            .distinctUntilChanged()
            .flatMapLatest { getPage() }
            .cachedIn(viewModelScope)
            .also { dataFlow ->
                _uiState.update {
                    return@update ListScreenUiState.Success(dataFlow)
                }
            }
    }

    override fun setSearchQuery(query: String) {
        _searchQuery.update { query }
        searchQueryProvider.setSearchQuery(dataTypeKey, query)
    }

    override fun onActiveChange(active: Boolean) {
        _isSearching.update { active }
    }

    override fun onSearch(query: String) {
        setSearchQuery(query)
        onActiveChange(!_isSearching.value)
        saveSearchHistory(query)
    }

    override fun setLastItemIndex(index: Int) {
        _lastItemIndex.update {
            savedStateHandle["${dataTypeKey}_last_item_index"] = index
            index
        }
    }

    override fun removeAllQuery() {
        _searchQuery.update { "" }
    }

    private fun saveSearchHistory(historyItem: String) = viewModelScope.launch {
        userPreferencesRepository.saveSearchHistory(dataTypeKey, historyItem)
    }
}