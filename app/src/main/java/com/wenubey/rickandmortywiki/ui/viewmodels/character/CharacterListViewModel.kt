package com.wenubey.rickandmortywiki.ui.viewmodels.character

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.CharacterRepository
import com.wenubey.domain.repository.SearchQueryProvider
import com.wenubey.domain.repository.SettingsRepository
import com.wenubey.rickandmortywiki.ui.di.IoDispatcher
import com.wenubey.rickandmortywiki.ui.di.MainDispatcher
import com.wenubey.rickandmortywiki.ui.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.wenubey.domain.model.Character

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    searchQueryProvider: SearchQueryProvider,
    settingsRepository: SettingsRepository,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<Character>(
    searchQueryProvider,
    settingsRepository,
    savedStateHandle,
    dataTypeKey = DataTypeKey.CHARACTER,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {
    override fun getPage(): Flow<PagingData<Character>> = characterRepository.getCharacterPage()
}