package com.wenubey.rickandmortywiki.ui.screens.location

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
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
import com.wenubey.rickandmortywiki.utils.isScrollingUp
import com.wenubey.rickandmortywiki.utils.isSystemInPortraitOrientation
import com.wenubey.rickandmortywiki.utils.makeToast
import com.wenubey.rickandmortywiki.ui.viewmodels.ListScreenUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.ListScreenEvents
import com.wenubey.rickandmortywiki.ui.viewmodels.user_pref.UserPreferencesViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.location.LocationListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocationListScreen(
    onLocationSelected: (id: Int) -> Unit,
    navigateUp: () -> Unit,
    lazyStaggeredGridState: LazyStaggeredGridState,
    lazyGridState: LazyGridState,
    onScrollUp: (Boolean) -> Unit,
    pagerState: PagerState,
    locationListViewModel: LocationListViewModel,
    events: ListScreenEvents
) {
    val context = LocalContext.current
    val userPrefViewModel: UserPreferencesViewModel = hiltViewModel()

    val locationUiState = locationListViewModel.uiState.collectAsState().value
    val userPrefUiState = userPrefViewModel.userPreferencesUserPrefUiState.collectAsState().value


    val searchQuery = locationListViewModel.searchQuery.collectAsState().value
    val active = locationListViewModel.isSearching.collectAsState().value
    val searchHistory = userPrefUiState.locationSearchHistory.searchHistory
    val isLinearLayout = userPrefUiState.linearLayout.isLinearLayout
    val isTopBarLocked = userPrefUiState.topBarLock.isTopBarLocked



    onScrollUp(
        if (isLinearLayout) {
            lazyGridState.isScrollingUp()
        } else {
            lazyStaggeredGridState.isScrollingUp()
        }
    )

    LaunchedEffect( lazyStaggeredGridState, lazyGridState, isLinearLayout, pagerState) {
        val index =if (isLinearLayout) {
            lazyGridState.firstVisibleItemIndex
        } else {
            lazyStaggeredGridState.firstVisibleItemIndex
        }
        locationListViewModel.setLastItemIndex(index)
        if (index > 0) {
            if (isLinearLayout) {
                lazyGridState.animateScrollToItem(index)
            } else {
                lazyStaggeredGridState.animateScrollToItem(index)
            }
        }

    }


    val isVisible = if (isTopBarLocked) {
        true
    } else {
        lazyStaggeredGridState.isScrollingUp()
    }

    val isLastItemIndexZero by remember {
        derivedStateOf {
            lazyStaggeredGridState.firstVisibleItemIndex == 0
        }
    }

    val scope = rememberCoroutineScope()

    val gridCount = if (isSystemInPortraitOrientation()) 1 else 2
    BackHandler {
        scope.launch {
            lazyStaggeredGridState.animateScrollToItem(0)
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
