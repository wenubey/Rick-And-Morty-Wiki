package com.wenubey.rickandmortywiki.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.LocationRepository
import com.wenubey.domain.repository.SearchQueryProvider
import com.wenubey.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LocationListViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val searchQueryProvider: SearchQueryProvider,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {


}


sealed interface LocationListUiState {
    data object Loading : LocationListUiState
    data class Error(val message: String) : LocationListUiState
    data class Success(val charactersFlow: Flow<PagingData<Location>>) : LocationListUiState
}
