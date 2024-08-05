package com.wenubey.rickandmortywiki.ui.viewmodels.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.domain.repository.LocationRepository
import com.wenubey.rickandmortywiki.ui.di.IoDispatcher
import com.wenubey.rickandmortywiki.ui.di.MainDispatcher
import com.wenubey.rickandmortywiki.ui.viewmodels.LocationDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LocationDetailViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _locationDetailUiState = MutableStateFlow<LocationDetailUiState>(
        value = LocationDetailUiState.Loading
    )
    val locationDetailUiState: StateFlow<LocationDetailUiState> = _locationDetailUiState.asStateFlow()

    fun getLocation(id: Int) = viewModelScope.launch(ioDispatcher) {
        locationRepository.getLocation(id)
            .onSuccess {  location ->
                withContext(mainDispatcher) {
                    Timber.d("getLocation:Success")
                    _locationDetailUiState.update {
                        return@update LocationDetailUiState.Success(
                            location = location,
                        )
                    }
                }
            }
            .onFailure { e ->
                withContext(mainDispatcher) {
                    Timber.e(e, "getLocation:Error")
                    _locationDetailUiState.update {
                        return@update LocationDetailUiState.Error(
                            e.message ?: UNKNOWN_ERROR
                        )
                    }
                }
            }
    }

    private companion object {
        const val UNKNOWN_ERROR = "Unknown error occurred."
    }
}

