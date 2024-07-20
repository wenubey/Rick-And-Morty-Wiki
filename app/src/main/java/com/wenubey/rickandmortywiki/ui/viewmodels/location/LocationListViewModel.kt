package com.wenubey.rickandmortywiki.ui.viewmodels.location

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.LocationRepository
import com.wenubey.domain.repository.SearchQueryProvider
import com.wenubey.domain.repository.SettingsRepository
import com.wenubey.rickandmortywiki.ui.di.IoDispatcher
import com.wenubey.rickandmortywiki.ui.di.MainDispatcher
import com.wenubey.rickandmortywiki.ui.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LocationListViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    searchQueryProvider: SearchQueryProvider,
    settingsRepository: SettingsRepository,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
): BaseViewModel<Location> (
    searchQueryProvider,
    settingsRepository,
    savedStateHandle,
    dataTypeKey = DataTypeKey.LOCATION,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {
    override fun getPage(): Flow<PagingData<Location>> = locationRepository.getLocationPage()
}

