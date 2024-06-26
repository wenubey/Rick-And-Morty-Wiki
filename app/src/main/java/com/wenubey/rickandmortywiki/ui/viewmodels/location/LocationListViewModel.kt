package com.wenubey.rickandmortywiki.ui.viewmodels.location

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.LocationRepository
import com.wenubey.domain.repository.SearchQueryProvider
import com.wenubey.domain.repository.UserPreferencesRepository
import com.wenubey.rickandmortywiki.ui.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LocationListViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    searchQueryProvider: SearchQueryProvider,
    userPreferencesRepository: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle,
): BaseViewModel<Location> (
    searchQueryProvider,
    userPreferencesRepository,
    savedStateHandle,
    dataTypeKey = DataTypeKey.LOCATION
) {
    override fun getPage(): Flow<PagingData<Location>> = locationRepository.getLocationPage()
}

