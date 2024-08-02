package com.wenubey.rickandmortywiki.ui.viewmodels.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.SettingsRepository
import com.wenubey.rickandmortywiki.ui.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel(), SettingsEvents {

    override val settingsUiState: StateFlow<SettingsUiState> =
        com.wenubey.data.combine(
            settingsRepository.isScreenLocked,
            settingsRepository.isLinearLayout,
            settingsRepository.isNightMode,
            settingsRepository.getSearchHistory(dataTypeKey = DataTypeKey.CHARACTER),
            settingsRepository.getSearchHistory(dataTypeKey = DataTypeKey.LOCATION),
            settingsRepository.isTopBarLocked,
        ) { isScreenLocked, isLinearLayout, isNightMode, characterSearchHistory, locationSearchHistory, isTopBarLocked ->
            SettingsUiState(
                screenLock = ScreenLock(isScreenLocked = isScreenLocked),
                linearLayout = LinearLayout(isLinearLayout = isLinearLayout),
                nightMode = NightMode(isNightMode = isNightMode),
                characterSearchHistory = CharacterSearchHistory(searchHistory = characterSearchHistory),
                locationSearchHistory = LocationSearchHistory(searchHistory = locationSearchHistory),
                topBarLock = TopBarLock(isTopBarLocked = isTopBarLocked),
            )
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState()
        )


    override fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            settingsRepository.saveLayoutPreference(isLinearLayout)
        }
    }

    override fun selectNightMode(isNightMode: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            settingsRepository.saveNightModePreference(isNightMode)
        }
    }

    override fun selectScreenLock(isScreenLocked: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            settingsRepository.saveScreenLockedPreference(isScreenLocked)
        }
    }

    override fun selectTopBarLock(isTopBarLocked: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            settingsRepository.saveTopBarLockedPreference(isTopBarLocked)
        }
    }

    override fun clearAllSearchHistory() {
        viewModelScope.launch(ioDispatcher) {
            settingsRepository.cleanAllSearchHistory()
        }
    }
}


