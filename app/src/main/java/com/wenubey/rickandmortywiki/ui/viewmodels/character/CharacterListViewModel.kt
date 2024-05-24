package com.wenubey.rickandmortywiki.ui.viewmodels.character

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.CharacterRepository
import com.wenubey.domain.repository.SearchQueryProvider
import com.wenubey.domain.repository.UserPreferencesRepository
import com.wenubey.rickandmortywiki.ui.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    searchQueryProvider: SearchQueryProvider,
    userPreferencesRepository: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle,
): BaseViewModel<Character>(
    searchQueryProvider,
    userPreferencesRepository,
    savedStateHandle,
    dataTypeKey = DataTypeKey.CHARACTER
) {
    override fun getPage(): Flow<PagingData<Character>> = characterRepository.getCharacterPage()
}