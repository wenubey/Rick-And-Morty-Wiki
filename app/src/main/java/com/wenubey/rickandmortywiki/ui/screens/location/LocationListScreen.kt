package com.wenubey.rickandmortywiki.ui.screens.location

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.wenubey.rickandmortywiki.ui.components.common.CustomProgressIndicator
import com.wenubey.rickandmortywiki.ui.components.common.CustomSearchBar
import com.wenubey.rickandmortywiki.ui.components.location.LocationListCard
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
    lazyGridState: LazyGridState,
    onScrollUp: (Boolean) -> Unit
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

    LaunchedEffect(Unit) {
        lazyGridState.animateScrollToItem(lastItemIndex)
    }

    onScrollUp(
        lazyGridState.isScrollingUp()
    )

    LaunchedEffect(lazyGridState, lazyGridState, isLinearLayout) {
        val index = lazyGridState.firstVisibleItemIndex
        locationViewModel.setLastItemIndex(index)
        lazyGridState.scrollToItem(index)
    }


    val isVisible = if (isTopBarLocked) {
        true
    } else {
        lazyGridState.isScrollingUp()
    }

    val isLastItemIndexZero by remember {
        derivedStateOf {
            lazyGridState.firstVisibleItemIndex == 0
        }
    }

    val scope = rememberCoroutineScope()


    BackHandler {
        scope.launch {
            lazyGridState.animateScrollToItem(0)
            if (isLastItemIndexZero) {
                navigateUp()
            }
        }
    }

    val gridColumns = if (isSystemInPortraitOrientation()) {
        if (isLinearLayout) {
            1
        } else {
            2
        }
    } else {
        2
    }


    when (locationUiState) {
        is LocationListUiState.Loading -> {
            CustomProgressIndicator()
        }

        is LocationListUiState.Error -> {
            // TODO add error screen
        }

        is LocationListUiState.Success -> {
            val locations = locationUiState.locationsFlow.collectAsLazyPagingItems()

            Column(
                modifier = Modifier
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

                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(gridColumns),
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
                            LocationListCard(
                                location = location,
                                onLocationSelected = {
                                    onLocationSelected(location.id)
                                }
                            )
                        } else {
                            // TODO add error screen about location is null
                        }
                    }
                }
            }

        }
    }
}
