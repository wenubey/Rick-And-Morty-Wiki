package com.wenubey.rickandmortywiki.ui.screens.character

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wenubey.rickandmortywiki.ui.components.character.detail.CharacterPropertiesComponent
import com.wenubey.rickandmortywiki.ui.components.character.detail.CharacterStatusDetailComponent
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.common.CustomProgressIndicator
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterDetailUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterDetailViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.LocationListViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPreferencesViewModel

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    onBackButtonPressed: () -> Unit,
    navigateToLocationScreen: () -> Unit,
    characterViewModel: CharacterDetailViewModel = hiltViewModel(),
    userPrefViewModel: UserPreferencesViewModel = hiltViewModel(),
    locationViewModel: LocationListViewModel = hiltViewModel()
) {
    val characterUiState = characterViewModel.characterDetailUiState.collectAsState().value
    val userPrefUiState =
        userPrefViewModel.userPreferencesUserPrefUiState.collectAsState().value

    val isTopBarLocked = userPrefUiState.topBarLock.isTopBarLocked
    LaunchedEffect(Unit) {
        characterViewModel.getCharacter(characterId)
    }

    val lazyListState = rememberLazyListState()

    when (val currentState = characterUiState) {


        is CharacterDetailUiState.Error -> {
            // TODO not yet implemented.
        }

        is CharacterDetailUiState.Loading -> {
            CustomProgressIndicator()
        }

        is CharacterDetailUiState.Success -> {

            val character = currentState.character

            val onLocationClicked: (String) -> Unit = { locationQuery ->
                locationViewModel.setSearchQuery(locationQuery)
                locationViewModel.onSearch(locationQuery)
                navigateToLocationScreen()
            }
            Scaffold(
                topBar = {
                    CommonTopAppBar(
                        isVisible = if (isTopBarLocked) true else lazyListState.canScrollForward ,
                        showNavigationIcon = true,
                        title = character.name,
                        onBackButtonPressed = onBackButtonPressed,
                        uiState = userPrefUiState,
                        onNightModeToggle = { isNightMode ->
                            userPrefViewModel.selectNightMode(isNightMode)
                        },
                        onTopBarLockToggle = { isTopBarLocked ->
                            userPrefViewModel.selectTopBarLock(isTopBarLocked)
                        },
                        onScreenLockToggle = { isScreenLocked ->
                            userPrefViewModel.selectScreenLock(isScreenLocked)
                        },
                        onLinearLayoutToggle = { isLinearLayout ->
                            userPrefViewModel.selectLayout(isLinearLayout)
                        },
                        clearAllSearchHistory = {
                            userPrefViewModel.clearAllSearchHistory()
                        },
                    )
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    state = lazyListState,
                ) {

                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    item {
                        CharacterStatusDetailComponent(
                            characterStatus = character.status,
                            imageUrl = character.imageUrl
                        )
                    }

                    item {
                        CharacterPropertiesComponent(
                            character = character,
                            onLocationClicked = onLocationClicked,
                        )
                    }


                }


            }
        }
    }


}





