package com.wenubey.rickandmortywiki.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.wenubey.rickandmortywiki.ui.HomeTabs
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.common.ScrollToTopFAB
import com.wenubey.rickandmortywiki.ui.components.common.TabScreenTabRow
import com.wenubey.rickandmortywiki.ui.components.pref_menu.UserPreferencesOption
import com.wenubey.rickandmortywiki.ui.screens.character.CharacterListScreen
import com.wenubey.rickandmortywiki.ui.screens.location.LocationListScreen
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPreferencesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabScreen(
    tabIndex: Int,
    onCharacterSelected: (Int) -> Unit,
    onLocationSelected: (Int) -> Unit,
    navigateUp: () -> Unit,
) {

    val pagerState =
        rememberPagerState(initialPage = tabIndex, pageCount = { HomeTabs.entries.size })
    val currentTabIndex by remember {
        derivedStateOf {
            pagerState.currentPage
        }
    }
    var isScrollUp by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    val userPrefViewModel: UserPreferencesViewModel = hiltViewModel()
    val userPrefUiState =
        userPrefViewModel.userPreferencesUserPrefUiState.collectAsState().value
    val isTopBarLocked = userPrefUiState.topBarLock.isTopBarLocked

    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = 0
    )
    val lazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = 0
    )

    val isVisible = if (isTopBarLocked) {
        true
    } else {
        isScrollUp
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                showNavigationIcon = false,
                isVisible = isVisible,
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
                userPreferencesOption = UserPreferencesOption.LIST,
            )
        },
        bottomBar = {
            TabScreenTabRow(pagerState = pagerState, currentTabIndex = currentTabIndex)
        },
        floatingActionButton = {
            ScrollToTopFAB(
                onClick =  {
                    scope.launch {
                        lazyGridState.animateScrollToItem(0)
                        lazyListState.animateScrollToItem(0)
                    }
                }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), state = pagerState
        ) { page ->
            when (page) {
                0 -> {
                    CharacterListScreen(
                        onCharacterSelected = onCharacterSelected,
                        navigateUp = navigateUp,
                        lazyListState = lazyListState,
                        lazyGridState = lazyGridState,
                        onScrollUp = {
                            isScrollUp = it
                        }
                    )
                }

                1 -> {
                    LocationListScreen(
                        onLocationSelected = onLocationSelected,
                        navigateUp = navigateUp,
                        lazyGridState = lazyGridState,
                        onScrollUp = {
                            isScrollUp = it
                        }
                    )
                }

                else -> {
                    // TODO Error screen not yet implemented.
                }
            }

        }
    }

}


