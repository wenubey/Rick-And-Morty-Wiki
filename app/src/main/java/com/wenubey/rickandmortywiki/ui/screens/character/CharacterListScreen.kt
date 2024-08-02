package com.wenubey.rickandmortywiki.ui.screens.character

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.components.character.list.CharacterGridCard
import com.wenubey.rickandmortywiki.ui.components.character.list.CharacterListCard
import com.wenubey.rickandmortywiki.ui.components.common.CustomProgressIndicator
import com.wenubey.rickandmortywiki.ui.components.common.CustomSearchBar
import com.wenubey.rickandmortywiki.ui.viewmodels.ListScreenEvents
import com.wenubey.rickandmortywiki.ui.viewmodels.ListScreenUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.character.CharacterListViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.settings.SettingsViewModel
import com.wenubey.rickandmortywiki.utils.isScrollingUp
import com.wenubey.rickandmortywiki.utils.isSystemInPortraitOrientation
import com.wenubey.rickandmortywiki.utils.makeToast
import kotlinx.coroutines.launch
import com.wenubey.domain.model.Character

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CharacterListScreen(
    onCharacterSelected: (id: Int) -> Unit,
    navigateUp: () -> Unit,
    onScrollUp: (Boolean) -> Unit,
    pagerState: PagerState,
    characterListViewModel: CharacterListViewModel,
    events: ListScreenEvents,
    onScrollToTopInvoked: Boolean,
) {
    val context = LocalContext.current
    val userPrefViewModel: SettingsViewModel = hiltViewModel()
    val userPrefUiState =
        userPrefViewModel.settingsUiState.collectAsState().value
    val characterUiState = characterListViewModel.uiState.collectAsState().value
    val lastItemIndex = characterListViewModel.lastItemIndex.collectAsState().value

    val searchQuery = characterListViewModel.searchQuery.collectAsState().value
    val active = characterListViewModel.isSearching.collectAsState().value
    val searchHistory = userPrefUiState.characterSearchHistory.searchHistory

    val isLinearLayout = userPrefUiState.linearLayout.isLinearLayout
    val isTopBarLocked = userPrefUiState.topBarLock.isTopBarLocked

    val lazyListState = rememberLazyListState()
    val lazyGridState = rememberLazyGridState()


    LaunchedEffect(key1 = onScrollToTopInvoked) {
        if (isLinearLayout) {
            lazyListState.animateScrollToItem(0)
        } else {
            lazyGridState.animateScrollToItem(0)
        }
    }



    LaunchedEffect(Unit) {
        if (isLinearLayout) {
            lazyListState.animateScrollToItem(lastItemIndex)
        } else {
            lazyGridState.animateScrollToItem(lastItemIndex)
        }
    }

    onScrollUp(
        if (isLinearLayout) {
           lazyListState.isScrollingUp()
        } else {
            lazyGridState.isScrollingUp()
        }
    )

    LaunchedEffect(lazyGridState, lazyListState, isLinearLayout, pagerState) {
        val index = when {
            isLinearLayout -> lazyListState.firstVisibleItemIndex
            else -> lazyGridState.firstVisibleItemIndex
        }
        events.setLastItemIndex(index)
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


    when (characterUiState) {
        is ListScreenUiState.Error<Character> -> {
            context.makeToast(R.string.error_screen_state)
        }

        is ListScreenUiState.Loading<Character> -> {
            CustomProgressIndicator()
        }

        is ListScreenUiState.Success<Character> -> {
            val characters = characterUiState.dataFlow.collectAsLazyPagingItems()
            Column(
                modifier = Modifier
                    .padding()
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
                    onRemoveAllClicked = events::removeAllQuery
                )
                if (isLinearLayout) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(12.dp),
                        state = lazyListState
                    ) {
                        items(
                            count = characters.itemCount,
                            key = characters.itemKey(),
                            contentType = characters.itemContentType()
                        ) { index ->
                            val character = characters[index]
                            if (character != null) {
                                CharacterListCard(
                                    character = character,
                                    onCharacterSelected = {
                                        onCharacterSelected(character.id)
                                    },
                                )
                            } else {
                                context.makeToast(R.string.error_character_is_null)
                            }
                        }
                        item {
                            if (characters.loadState.append is LoadState.Loading) {
                                CustomProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally),
                                    indicatorSize = 50.dp
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
                            count = characters.itemCount,
                            key = characters.itemKey(),
                            contentType = characters.itemContentType()
                        ) { index ->
                            val character = characters[index]
                            if (character != null) {
                                if (isSystemInPortraitOrientation()) {
                                    CharacterGridCard(
                                        character = character,
                                        onCardClick = { onCharacterSelected(character.id) })
                                } else {
                                    CharacterListCard(
                                        character = character,
                                        onCharacterSelected = {
                                            onCharacterSelected(character.id)
                                        },
                                    )
                                }

                            }
                        }
                        item(
                            span = {
                                GridItemSpan(2)
                            }
                        ) {
                            if (characters.loadState.append is LoadState.Loading) {

                                CustomProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally),
                                    indicatorSize = 100.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}