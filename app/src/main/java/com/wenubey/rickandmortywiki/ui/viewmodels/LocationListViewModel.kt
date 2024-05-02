package com.wenubey.rickandmortywiki.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.LocationRepository
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
class LocationListViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val searchQueryProvider: SearchQueryProvider,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _locationListUiState = MutableStateFlow<LocationListUiState>(
        value = LocationListUiState.Loading
    )
    val locationListUiState: StateFlow<LocationListUiState> =
        _locationListUiState.asStateFlow()

    private val _searchQuery = MutableStateFlow(searchQueryProvider.getLocationSearchQuery())
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    init {
        locationPagingFlow()
    }


    private fun locationPagingFlow() {
        _searchQuery
            .debounce(500)
            .distinctUntilChanged()
            .flatMapLatest { _ ->
                locationRepository.getLocationPage()
            }
            .cachedIn(viewModelScope).also { locationsFlow ->
                _locationListUiState.update {
                    return@update LocationListUiState.Success(locationsFlow = locationsFlow)
                }
            }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.update {
            return@update query
        }
        searchQueryProvider.setLocationSearchQuery(query)
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
        userPreferencesRepository.saveLocationSearchHistory(historyItem)
    }
}


sealed interface LocationListUiState {
    data object Loading : LocationListUiState
    data class Error(val message: String) : LocationListUiState
    data class Success(val locationsFlow: Flow<PagingData<Location>>) : LocationListUiState
}
