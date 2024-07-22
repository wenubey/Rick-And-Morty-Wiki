package com.wenubey.rickandmortywiki.ui.screens.location

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.components.common.CustomProgressIndicator
import com.wenubey.rickandmortywiki.ui.components.common.CustomSearchBar
import com.wenubey.rickandmortywiki.ui.components.location.LocationListCard
import com.wenubey.rickandmortywiki.ui.viewmodels.ListScreenEvents
import com.wenubey.rickandmortywiki.ui.viewmodels.ListScreenUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.location.LocationListViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.settings.SettingsViewModel
import com.wenubey.rickandmortywiki.utils.isScrollingUp
import com.wenubey.rickandmortywiki.utils.isSystemInPortraitOrientation
import com.wenubey.rickandmortywiki.utils.makeToast
import kotlinx.coroutines.launch

@Composable
fun LocationListScreen(
    onLocationSelected: (id: Int) -> Unit,
    navigateUp: () -> Unit,
    onScrollUp: (Boolean) -> Unit,
    locationListViewModel: LocationListViewModel,
    events: ListScreenEvents,
    onScrollToTopInvoked: Boolean,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val gridCount = if (isSystemInPortraitOrientation()) 1 else 2

    // User Preferences
    val userPrefViewModel: SettingsViewModel = hiltViewModel()
    val userPrefUiState = userPrefViewModel.settingsUiState.collectAsState().value
    val searchHistory = userPrefUiState.locationSearchHistory.searchHistory
    val isLinearLayout = userPrefUiState.linearLayout.isLinearLayout
    val isTopBarLocked = userPrefUiState.topBarLock.isTopBarLocked

    // Location List
    val locationUiState = locationListViewModel.uiState.collectAsState().value
    val lastItemIndex = locationListViewModel.lastItemIndex.collectAsState().value
    val searchQuery = locationListViewModel.searchQuery.collectAsState().value
    val active = locationListViewModel.isSearching.collectAsState().value


    val lazyGridState = rememberLazyGridState()
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()

    LaunchedEffect(key1 = onScrollToTopInvoked) {

        if (isLinearLayout && onScrollToTopInvoked) {
            lazyGridState.animateScrollToItem(0)
        } else {
           // lazyStaggeredGridState.animateScrollToItem(0)
        }
    }
    LaunchedEffect(lastItemIndex) {
        if (lastItemIndex != 0) {
            if (isLinearLayout) {
                lazyGridState.animateScrollToItem(lastItemIndex)
            } else {
                lazyStaggeredGridState.animateScrollToItem(lastItemIndex)
            }
        }
    }

    val isVisible = if (isTopBarLocked) {
        true
    } else {
        if (isLinearLayout) {
            lazyGridState.isScrollingUp()
        } else {
            lazyStaggeredGridState.isScrollingUp()
        }
    }

    onScrollUp(isVisible)

    DisposableEffect(Unit) {
        onDispose {
            val index = when {
                isLinearLayout -> lazyGridState.firstVisibleItemIndex
                else -> lazyStaggeredGridState.firstVisibleItemIndex
            }
            events.setLastItemIndex(index)
        }
    }

    val isLastItemIndexZero by remember {
        derivedStateOf {
            if (isLinearLayout) {
                lazyGridState.firstVisibleItemIndex == 0
            } else {
                lazyStaggeredGridState.firstVisibleItemIndex == 0
            }
        }
    }
    BackHandler {
        scope.launch {
            if (isLinearLayout) {
                lazyGridState.animateScrollToItem(0)
            } else {
                lazyStaggeredGridState.animateScrollToItem(0)
            }
            if (isLastItemIndexZero) {
                navigateUp()
            }
        }
    }

    when (locationUiState) {
        is ListScreenUiState.Loading -> {
            CustomProgressIndicator()
        }
        is ListScreenUiState.Error -> {
            context.makeToast(R.string.error_screen_state)
        }
        is ListScreenUiState.Success -> {
            val locations = locationUiState.dataFlow.collectAsLazyPagingItems()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
            ) {
                CustomSearchBar(
                    isVisible = isVisible,
                    searchQuery = searchQuery,
                    active = active,
                    onActiveChange = events::onActiveChange,
                    onSearch = events::onSearch,
                    setSearchQuery = events::setSearchQuery,
                    searchHistory = searchHistory,
                    onRemoveAllClicked = events::removeAllQuery,
                )
                if (isLinearLayout) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(gridCount),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(12.dp),
                        state = lazyGridState
                    ) {
                        items(
                            count = locations.itemCount,
                            key = locations.itemKey(),
                            contentType = locations.itemContentType()
                        ) { index ->
                            val location = locations[index]
                            if (location != null) {
                                LocationListCard(
                                    location = location,
                                    onLocationSelected = {
                                        onLocationSelected(location.id)
                                    },
                                )
                            } else {
                                context.makeToast(R.string.error_location_is_null)
                            }
                        }
                        item {
                            if (locations.loadState.append is LoadState.Loading) {
                                CustomProgressIndicator(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                } else {
                    LazyVerticalStaggeredGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = StaggeredGridCells.Adaptive(150.dp),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp),
                        state = lazyStaggeredGridState
                    ) {
                        items(
                            count = locations.itemCount,
                            key = locations.itemKey(),
                            contentType = locations.itemContentType()
                        ) { index ->
                            val location = locations[index]
                            if (location != null) {
                                LocationListCard(
                                    location = location,
                                    onLocationSelected = {
                                        onLocationSelected(location.id)
                                    }
                                )
                            } else {
                                context.makeToast(R.string.error_location_is_null)
                            }
                        }
                        item {
                            if (locations.loadState.append is LoadState.Loading) {
                                CustomProgressIndicator(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
