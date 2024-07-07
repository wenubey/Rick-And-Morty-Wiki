package com.wenubey.rickandmortywiki.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.utils.HomeTabs
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.common.CopyRightView
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabScreen(
    tabIndex: Int,
    onCharacterSelected: (Int) -> Unit,
    onLocationSelected: (Int) -> Unit,
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
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

    val userPreferencesViewModel: SettingsViewModel = hiltViewModel()
    val events: SettingsEvents = userPreferencesViewModel

    val userPrefUiState =
        userPreferencesViewModel.settingsUiState.collectAsState().value
    val isTopBarLocked = userPrefUiState.topBarLock.isTopBarLocked
    val isLinearLayout = userPrefUiState.linearLayout.isLinearLayout

    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = 0
    )
    val lazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = 0
    )
    val lazyStaggeredGridState = rememberLazyStaggeredGridState(
        initialFirstVisibleItemIndex = 0
    )

    val isVisible = if (isTopBarLocked) {
        true
    } else {
        isScrollUp
    }

    val scrollToFirstItem: () -> Unit = {
        scope.launch {
            when (pagerState.currentPage) {
                0 -> {
                    if (isLinearLayout) {
                        lazyListState.animateScrollToItem(0)
                    } else {
                        lazyGridState.animateScrollToItem(0)
                    }
                }

                1 -> {
                    if (isLinearLayout) {
                        lazyGridState.animateScrollToItem(0)
                    } else {
                        lazyStaggeredGridState.animateScrollToItem(0)
                    }
                }

            }
        }
    }
    var isCopyRightDialogShowed by remember {
        mutableStateOf(false)
    }
    var isHowToUseDialogShowed by remember {
        mutableStateOf(false)
    }
    if (isCopyRightDialogShowed) {
        CopyRightView(onDismissRequest = { isCopyRightDialogShowed = !isCopyRightDialogShowed })
    }
    if (isHowToUseDialogShowed) {
        HowToUseScreen(onDismissRequest = {
            isHowToUseDialogShowed = !isHowToUseDialogShowed
        })
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
                onClick = scrollToFirstItem
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
                    val characterViewModel: CharacterListViewModel = hiltViewModel()
                    CharacterListScreen(
                        onCharacterSelected = onCharacterSelected,
                        navigateUp = navigateUp,
                        lazyListState = lazyListState,
                        lazyGridState = lazyGridState,
                        onScrollUp = { isScrollUp = it },
                        pagerState = pagerState,
                        characterListViewModel = characterViewModel,
                        events = characterViewModel,
                    )
                }

                1 -> {
                    val locationViewModel: LocationListViewModel = hiltViewModel()
                    LocationListScreen(
                        onLocationSelected = onLocationSelected,
                        navigateUp = navigateUp,
                        lazyStaggeredGridState = lazyStaggeredGridState,
                        onScrollUp = {
                            isScrollUp = it
                        },
                        pagerState = pagerState,
                        lazyGridState = lazyGridState,
                        locationListViewModel = locationViewModel,
                        events = locationViewModel
                    )
                }

                2 -> {
                    SettingsScreen(uiState = userPrefUiState, events = events)

                }

                else -> {
                    context.makeToast(R.string.error_screen_not_found)
                }
            }

        }
    }


}



