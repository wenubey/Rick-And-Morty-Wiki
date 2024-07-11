package com.wenubey.rickandmortywiki.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.utils.HomeTabs
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.common.ScrollToTopFAB
import com.wenubey.rickandmortywiki.ui.components.common.TabScreenTabRow
import com.wenubey.rickandmortywiki.utils.makeToast
import com.wenubey.rickandmortywiki.ui.screens.character.CharacterListScreen
import com.wenubey.rickandmortywiki.ui.screens.location.LocationListScreen
import com.wenubey.rickandmortywiki.ui.screens.settings.SettingsScreen
import com.wenubey.rickandmortywiki.ui.viewmodels.settings.SettingsViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.character.CharacterListViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.location.LocationListViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.settings.SettingsEvents

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabScreen(
    tabIndex: Int,
    onCharacterSelected: (Int) -> Unit,
    onLocationSelected: (Int) -> Unit,
    navigateUp: () -> Unit,
    onNavigateToCopyrightScreen: () -> Unit,
    onNavigateToHowToUseScreen: () -> Unit,
) {
    val context = LocalContext.current
    val pagerState =
        rememberPagerState(initialPage = tabIndex, pageCount = { HomeTabs.entries.size })
    val currentTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    // Scrolling States
    var isCharacterScreenScrolledUp by remember { mutableStateOf(false) }
    var isLocationScreenScrolledUp by remember { mutableStateOf(false) }
    var onScrollToFirstItemInvoked by remember { mutableStateOf(false) }

    // User Preferences
    val userPreferencesViewModel: SettingsViewModel = hiltViewModel()
    val events: SettingsEvents = userPreferencesViewModel
    val userPrefUiState =
        userPreferencesViewModel.settingsUiState.collectAsState().value
    val isTopBarLocked = userPrefUiState.topBarLock.isTopBarLocked

    // Top App Bar Visibility
    val isVisible = when {
        isTopBarLocked -> true
        pagerState.currentPage == 2 -> true
        pagerState.currentPage == 0 -> isCharacterScreenScrolledUp
        pagerState.currentPage == 1 -> isLocationScreenScrolledUp
        else -> true
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                showNavigationIcon = false,
                isVisible = isVisible,
            )
        },
        bottomBar = {
            TabScreenTabRow(pagerState = pagerState, currentTabIndex = currentTabIndex)
        },
        floatingActionButton = {
            val isUserOnTheSettingsScreen = pagerState.currentPage != 2
            ScrollToTopFAB(
                isUserOnTheSettingsScreen = isUserOnTheSettingsScreen,
                onClick = { onScrollToFirstItemInvoked = !onScrollToFirstItemInvoked }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = pagerState
        ) { page ->
            when (page) {
                0 -> {
                    val characterViewModel: CharacterListViewModel = hiltViewModel()
                    CharacterListScreen(
                        onCharacterSelected = onCharacterSelected,
                        navigateUp = navigateUp,
                        onScrollUp = { isCharacterScreenScrolledUp = it },
                        pagerState = pagerState,
                        characterListViewModel = characterViewModel,
                        events = characterViewModel,
                        onScrollToTopInvoked = onScrollToFirstItemInvoked
                    )
                }
                1 -> {
                    val locationViewModel: LocationListViewModel = hiltViewModel()
                    LocationListScreen(
                        onLocationSelected = onLocationSelected,
                        navigateUp = navigateUp,
                        onScrollUp = { isLocationScreenScrolledUp = it },
                        locationListViewModel = locationViewModel,
                        events = locationViewModel,
                        onScrollToTopInvoked = onScrollToFirstItemInvoked
                    )
                }
                2 -> {
                    SettingsScreen(
                        uiState = userPrefUiState,
                        events = events,
                        onNavigateToCopyrightScreen = onNavigateToCopyrightScreen,
                        onNavigateToHowToUseScreen = onNavigateToHowToUseScreen,
                    )

                }
                else -> {
                    context.makeToast(R.string.error_screen_not_found)
                }
            }

        }
    }


}



