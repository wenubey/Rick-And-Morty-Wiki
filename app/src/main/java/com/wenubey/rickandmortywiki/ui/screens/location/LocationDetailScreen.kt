package com.wenubey.rickandmortywiki.ui.screens.location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Location
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.components.character.detail.DetailHeaderComponent
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.common.CustomProgressIndicator
import com.wenubey.rickandmortywiki.ui.components.location.LocationDetailComponent
import com.wenubey.rickandmortywiki.ui.components.location.LocationDetailResidentsComponents
import com.wenubey.rickandmortywiki.ui.components.pref_menu.UserPreferencesOption
import com.wenubey.rickandmortywiki.ui.isSystemInPortraitOrientation
import com.wenubey.rickandmortywiki.ui.makeToast
import com.wenubey.rickandmortywiki.ui.shrinkParentheses
import com.wenubey.rickandmortywiki.ui.viewmodels.LocationDetailUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.location.LocationDetailViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.user_pref.UserPreferencesViewModel

@Composable
fun LocationDetailScreen(
    viewModel: LocationDetailViewModel = hiltViewModel(),
    userPrefViewModel: UserPreferencesViewModel = hiltViewModel(),
    locationId: Int,
    onBackButtonPressed: () -> Unit,
    onCharacterSelected: (Int) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getLocation(locationId)
    }

    val detailUiState = viewModel.locationDetailUiState.collectAsState().value
    val userPrefUiState = userPrefViewModel.userPreferencesUserPrefUiState.collectAsState().value


    when (detailUiState) {
        is LocationDetailUiState.Error -> {
            context.makeToast(R.string.error_screen_state)
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
                        title = location.name.shrinkParentheses(),
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
                if (isSystemInPortraitOrientation()) {
                    LocationDetailPortraitScreen(
                        paddingValues = paddingValues,
                        location = location,
                        residents = residents,
                        onCharacterSelected = onCharacterSelected
                    )
                } else {
                    LocationDetailLandscapeScreen(
                        paddingValues = paddingValues,
                        location = location,
                        residents = residents,
                        onCharacterSelected = onCharacterSelected
                    )
                }
            }
        }
    }
}

@Composable
fun LocationDetailPortraitScreen(
    paddingValues: PaddingValues,
    location: Location,
    residents: List<Character>,
    onCharacterSelected: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LocationDetailComponent(
            modifier = Modifier
                .fillMaxWidth(),
            location = location,
        )
        DetailHeaderComponent(headerTitle = stringResource(R.string.residents_header))
        LocationDetailResidentsComponents(
            residents = residents,
            onCharacterSelected = onCharacterSelected
        )
    }
}


@Composable
fun LocationDetailLandscapeScreen(
    paddingValues: PaddingValues,
    location: Location,
    residents: List<Character>,
    onCharacterSelected: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LocationDetailComponent(
            location = location,
//            modifier = Modifier.fillMaxHeight()
        )
        Column(modifier = Modifier.weight(1f)) {
            DetailHeaderComponent(headerTitle = stringResource(id = R.string.residents_header))
            LocationDetailResidentsComponents(
                residents = residents,
                onCharacterSelected = onCharacterSelected
            )
        }

    }
}



