package com.wenubey.rickandmortywiki.ui.viewmodels.user_pref

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.data.combine
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel(), UserPreferencesEvents {

    override val userPreferencesUserPrefUiState: StateFlow<UserPrefUiState> =
        combine(
            userPreferencesRepository.isScreenLocked,
            userPreferencesRepository.isLinearLayout,
            userPreferencesRepository.isNightMode,
            userPreferencesRepository.getSearchHistory(dataTypeKey = DataTypeKey.CHARACTER),
            userPreferencesRepository.getSearchHistory(dataTypeKey = DataTypeKey.LOCATION),
            userPreferencesRepository.isTopBarLocked,
        ) { isScreenLocked, isLinearLayout, isNightMode, characterSearchHistory, locationSearchHistory, isTopBarLocked ->
            UserPrefUiState(
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
            initialValue = UserPrefUiState()
        )


    override fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }

    override fun selectNightMode(isNightMode: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveNightModePreference(isNightMode)
        }
    }

    override fun selectScreenLock(isScreenLocked: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveScreenLockedPreference(isScreenLocked)
        }
    }

    override fun selectTopBarLock(isTopBarLocked: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveTopBarLockedPreference(isTopBarLocked)
        }
    }

    override fun clearAllSearchHistory() {
        viewModelScope.launch {
            userPreferencesRepository.cleanAllSearchHistory()
        }
    }
}


