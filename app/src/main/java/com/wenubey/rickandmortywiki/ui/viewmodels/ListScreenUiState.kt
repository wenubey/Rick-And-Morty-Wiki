package com.wenubey.rickandmortywiki.ui.viewmodels

import androidx.paging.PagingData
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Location
import kotlinx.coroutines.flow.Flow



sealed interface ListScreenUiState<T>  {
    data class Loading<T>(val loadingMessage: String = "") : ListScreenUiState<T>
    data class Error<T>(val message: String) : ListScreenUiState<T>
    data class Success<T : Any>(val dataFlow: Flow<PagingData<T>>) : ListScreenUiState<T>
}

sealed interface LocationDetailUiState {
    data object Loading : LocationDetailUiState
    data class Error(val message: String) : LocationDetailUiState
    data class Success(val location: Location, val residents: List<Character>) :
        LocationDetailUiState
}

sealed interface CharacterDetailUiState {
    data object Loading : CharacterDetailUiState
    data class Error(val message: String) : CharacterDetailUiState
    data class Success(
        val character: Character = Character.default()
    ): CharacterDetailUiState
}