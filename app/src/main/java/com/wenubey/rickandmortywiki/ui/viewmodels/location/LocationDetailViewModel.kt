package com.wenubey.rickandmortywiki.ui.viewmodels.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.data.getIdFromUrls
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationDetailViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
) : ViewModel() {

    private val _locationDetailUiState = MutableStateFlow<LocationDetailUiState>(
        value = LocationDetailUiState.Loading
    )
    val locationDetailUiState: StateFlow<LocationDetailUiState> = _locationDetailUiState.asStateFlow()

    fun getLocation(id: Int) = viewModelScope.launch {
        locationRepository.getLocation(id)
            .onSuccess {  location ->
                val residentIds = location.residents.getIdFromUrls()
                val residents = locationRepository.getCharactersById(residentIds).getOrNull() ?: listOf()

                _locationDetailUiState.update {
                    return@update LocationDetailUiState.Success(
                        location = location,
                        residents = residents
                    )
                }
            }
            .onFailure { e ->
                    _locationDetailUiState.update {
                        return@update LocationDetailUiState.Error(
                            e.message ?: "Unknown error occurred"
                        )
                    }
            }
    }
}

sealed interface LocationDetailUiState {
    data object Loading : LocationDetailUiState
    data class Error(val message: String) : LocationDetailUiState
    data class Success(val location: Location, val residents: List<Character>) :
        LocationDetailUiState
}
