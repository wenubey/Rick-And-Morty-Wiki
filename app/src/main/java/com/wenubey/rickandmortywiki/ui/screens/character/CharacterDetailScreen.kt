package com.wenubey.rickandmortywiki.ui.screens.character

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.components.character.detail.CharacterPropertiesComponent
import com.wenubey.rickandmortywiki.ui.components.character.detail.CharacterStatusDetailComponent
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.common.CustomProgressIndicator
import com.wenubey.rickandmortywiki.utils.isSystemInPortraitOrientation
import com.wenubey.rickandmortywiki.utils.makeToast
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterDetailUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.character.CharacterDetailViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.location.LocationListViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.settings.SettingsViewModel
import com.wenubey.domain.model.Character

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    onBackButtonPressed: () -> Unit,
    navigateToLocationScreen: () -> Unit,
    characterViewModel: CharacterDetailViewModel = hiltViewModel(),
    userPrefViewModel: SettingsViewModel = hiltViewModel(),
    locationViewModel: LocationListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val characterUiState = characterViewModel.characterDetailUiState.collectAsState().value
    val userPrefUiState =
        userPrefViewModel.settingsUiState.collectAsState().value

    val isTopBarLocked = userPrefUiState.topBarLock.isTopBarLocked
    LaunchedEffect(Unit) {
        characterViewModel.getCharacter(characterId)
    }
    val lazyListState = rememberLazyListState()

    when (val currentState = characterUiState) {
        is CharacterDetailUiState.Error -> {
            context.makeToast(R.string.error_screen_state)
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
                        isVisible = if (isTopBarLocked) true else lazyListState.canScrollForward,
                        showNavigationIcon = true,
                        title = character.name,
                        onBackButtonPressed = onBackButtonPressed,
                    )
                }
            ) { paddingValues ->
                if (isSystemInPortraitOrientation()) {
                    CharacterDetailPortraitScreen(
                        paddingValues = paddingValues,
                        lazyListState = lazyListState,
                        character = character,
                        onLocationClicked = onLocationClicked
                    )
                } else {
                    CharacterDetailLandscapeScreen(
                        paddingValues = paddingValues,
                        lazyListState = lazyListState,
                        character = character,
                        onLocationClicked = onLocationClicked
                    )
                }


            }
        }
    }


}

@Composable
fun CharacterDetailPortraitScreen(
    paddingValues: PaddingValues,
    lazyListState: LazyListState,
    character: Character,
    onLocationClicked: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        state = lazyListState,
    ) {

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                CharacterStatusDetailComponent(
                    characterStatus = character.status,
                    imageUrl = character.imageUrl
                )
            }
        }

        item {
            CharacterPropertiesComponent(
                character = character,
                onLocationClicked = onLocationClicked,
            )
        }


    }
}

@Composable
fun CharacterDetailLandscapeScreen(
    paddingValues: PaddingValues,
    lazyListState: LazyListState,
    character: Character,
    onLocationClicked: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CharacterStatusDetailComponent(
            characterStatus = character.status,
            imageUrl = character.imageUrl
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            state = lazyListState
        ) {
            item {
                CharacterPropertiesComponent(
                    character = character,
                    onLocationClicked = onLocationClicked,
                )
            }


        }


    }
}





