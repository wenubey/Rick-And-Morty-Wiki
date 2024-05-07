package com.wenubey.rickandmortywiki.ui.screens.location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wenubey.rickandmortywiki.ui.components.character.detail.DetailHeaderComponent
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.common.CustomProgressIndicator
import com.wenubey.rickandmortywiki.ui.components.location.LocationDetailComponent
import com.wenubey.rickandmortywiki.ui.components.location.LocationDetailResidentsComponents
import com.wenubey.rickandmortywiki.ui.components.pref_menu.UserPreferencesOption
import com.wenubey.rickandmortywiki.ui.viewmodels.LocationDetailUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.LocationDetailViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPreferencesViewModel

@Composable
fun LocationDetailScreen(
    viewModel: LocationDetailViewModel = hiltViewModel(),
    userPrefViewModel: UserPreferencesViewModel = hiltViewModel(),
    locationId: Int,
    onBackButtonPressed: () -> Unit,
    onCharacterSelected: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getLocation(locationId)
    }

    val detailUiState = viewModel.locationDetailUiState.collectAsState().value
    val userPrefUiState = userPrefViewModel.userPreferencesUserPrefUiState.collectAsState().value


    when (detailUiState) {
        is LocationDetailUiState.Error -> {
            // TODO not yet implemented.
        }

        is LocationDetailUiState.Loading -> {
            CustomProgressIndicator()
        }

        is LocationDetailUiState.Success -> {
            val location = detailUiState.location
            val residents = detailUiState.residents
            // TODO Handle Landscape UI
            Scaffold(
                topBar = {
                    CommonTopAppBar(
                        isVisible = true,
                        title = location.name,
                        showNavigationIcon = true,
                        onBackButtonPressed = onBackButtonPressed,
                        uiState = userPrefUiState,
                        onNightModeToggle = { nightMode ->
                            userPrefViewModel.selectNightMode(nightMode)
                        },
                        onScreenLockToggle = { screenLock ->
                            userPrefViewModel.selectScreenLock(screenLock)
                        },
                        onTopBarLockToggle = { isTopBarLocked ->
                            userPrefViewModel.selectTopBarLock(isTopBarLocked)
                        },
                        userPreferencesOption = UserPreferencesOption.DETAIL
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LocationDetailComponent(location = location)
                    DetailHeaderComponent(headerTitle = "RESIDENTS", fontSize = 24)
                    LocationDetailResidentsComponents(residents = residents, onCharacterSelected = onCharacterSelected)
                }
            }
        }
    }
}



