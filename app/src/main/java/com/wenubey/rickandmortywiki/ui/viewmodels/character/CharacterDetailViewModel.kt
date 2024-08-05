package com.wenubey.rickandmortywiki.ui.viewmodels.character


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.domain.repository.CharacterRepository
import com.wenubey.rickandmortywiki.ui.di.IoDispatcher
import com.wenubey.rickandmortywiki.ui.di.MainDispatcher
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterDetailUiState
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
class CharacterDetailViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _characterDetailUiState = MutableStateFlow<CharacterDetailUiState>(
        value = CharacterDetailUiState.Loading
    )
    val characterDetailUiState: StateFlow<CharacterDetailUiState> =
        _characterDetailUiState.asStateFlow()


    fun getCharacter(id: Int) = viewModelScope.launch(ioDispatcher) {
        _characterDetailUiState.update { return@update CharacterDetailUiState.Loading }
        characterRepository.getCharacter(id)
            .onSuccess { character ->
                Timber.d("getCharacter:Success")
                withContext(mainDispatcher) {
                    _characterDetailUiState.update {
                        return@update CharacterDetailUiState.Success(
                            character = character,
                        )
                    }
                }

            }
            .onFailure { exception ->
                withContext(mainDispatcher) {
                    Timber.e(exception, "getCharacter:Error")
                    _characterDetailUiState.update {
                        return@update CharacterDetailUiState.Error(
                            message = exception.localizedMessage ?: UNKNOWN_ERROR
                        )
                    }
                }
            }
    }

    private companion object {
        const val UNKNOWN_ERROR = "Unknown error occurred."
    }
}
