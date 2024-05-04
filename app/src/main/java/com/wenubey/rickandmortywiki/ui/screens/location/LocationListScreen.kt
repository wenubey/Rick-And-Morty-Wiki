package com.wenubey.rickandmortywiki.ui.screens.location

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.wenubey.rickandmortywiki.ui.components.character.list.CharacterGridCard
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.common.CustomProgressIndicator
import com.wenubey.rickandmortywiki.ui.components.common.CustomSearchBar
import com.wenubey.rickandmortywiki.ui.isScrollingUp
import com.wenubey.rickandmortywiki.ui.isSystemInPortraitOrientation
import com.wenubey.rickandmortywiki.ui.viewmodels.LocationListUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.LocationListViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPreferencesViewModel
import kotlinx.coroutines.launch

@Composable
fun LocationListScreen(
    onLocationSelected: (id: Int) -> Unit,
    navigateUp: () -> Unit,
) {
    val locationViewModel: LocationListViewModel = hiltViewModel()
    val userPrefViewModel: UserPreferencesViewModel = hiltViewModel()

    val locationUiState = locationViewModel.locationListUiState.collectAsState().value
    val userPrefUiState = userPrefViewModel.userPreferencesUserPrefUiState.collectAsState().value

    val lastItemIndex = locationViewModel.lastItemIndex.collectAsState().value

    val searchQuery = locationViewModel.searchQuery.collectAsState().value
    val active = locationViewModel.isSearching.collectAsState().value
    val searchHistory = userPrefUiState.locationSearchHistory.searchHistory
    val isLinearLayout = userPrefUiState.linearLayout.isLinearLayout
    val isTopBarLocked = userPrefUiState.topBarLock.isTopBarLocked

    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = lastItemIndex
    )
    val lazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = lastItemIndex
    )

    LaunchedEffect(lazyGridState, lazyListState, isLinearLayout) {
        val index = when {
            isLinearLayout -> lazyGridState.firstVisibleItemIndex
            else -> lazyListState.firstVisibleItemIndex
        }
        locationViewModel.setLastItemIndex(index)
        if (isLinearLayout) {
            lazyListState.scrollToItem(index)
        } else {
            lazyGridState.scrollToItem(index)
        }
    }

    val isVisible = if (isTopBarLocked) {
        true
    } else {
        if (isLinearLayout) {
            lazyListState.isScrollingUp()
        } else {
            lazyGridState.isScrollingUp()
        }
    }

    val isLastItemIndexZero by remember {
        derivedStateOf {
            if (isLinearLayout) {
                lazyListState.firstVisibleItemIndex == 0
            } else {
                lazyGridState.firstVisibleItemIndex == 0
            }
        }
    }

    val scope = rememberCoroutineScope()

    BackHandler {
        scope.launch {
            if (isLinearLayout) {
                lazyListState.animateScrollToItem(0)
            } else {
                lazyGridState.animateScrollToItem(0)
            }
            if (isLastItemIndexZero) {
                navigateUp()
            }
        }
    }


    when(locationUiState) {
        is LocationListUiState.Loading -> {
            CustomProgressIndicator()
        }
        is LocationListUiState.Error -> {
            // TODO add error screen
        }
        is LocationListUiState.Success -> {
            val locations = locationUiState.locationsFlow.collectAsLazyPagingItems()
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
                    )
                }
            ) { paddingValues ->

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(Color.Transparent),
                ) {
                    CustomSearchBar(
                        isVisible = isVisible,
                        searchQuery = searchQuery,
                        active = active,
                        onActiveChange = locationViewModel::onActiveChange,
                        onSearch = locationViewModel::onSearch,
                        setSearchQuery = locationViewModel::setSearchQuery,
                        searchHistory = searchHistory,
                    )
                    if (isLinearLayout) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(12.dp),
                            state = lazyListState
                        ) {
                            items(
                                count = locations.itemCount,
                                key = locations.itemKey(),
                                contentType = locations.itemContentType()
                            ) { index ->
                                val location = locations[index]
                                if (location != null) {
                                    // TODO Location List Card
                                    Text(text = location.name)
                                } else {
                                    // TODO add error screen about location is null
                                }
                            }
                            item {
                                if (locations.loadState.append is LoadState.Loading) {
                                    CustomProgressIndicator(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(16.dp),
                            state = lazyGridState
                        ) {
                            items(
                                count = locations.itemCount,
                                key = locations.itemKey(),
                                contentType = locations.itemContentType()
                            ) { index ->
                                val location = locations[index]
                                if (location != null) {
                                    if (isSystemInPortraitOrientation()) {
                                        // TODO location grid card
                                    } else {
                                        // TODO Location List Card
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}