package com.wenubey.rickandmortywiki.ui.screens

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.wenubey.rickandmortywiki.ui.components.character.CharacterGridCard
import com.wenubey.rickandmortywiki.ui.components.character.CharacterListCard
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.common.CustomProgressIndicator
import com.wenubey.rickandmortywiki.ui.components.common.SearchBar
import com.wenubey.rickandmortywiki.ui.isSystemInPortraitOrientation
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterListUiState

@Composable
fun CharacterListScreen(
    isLinearLayout: Boolean,
    characterUiState: CharacterListUiState,
    onCharacterSelected: (id: Int) -> Unit,
    setLastItemIndex: (index: Int) -> Unit,
    lastItemIndex: Int?,
    searchQuery: String,
    setSearchQuery: (String) -> Unit,
) {

    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = lastItemIndex ?: 0
    )
    val lazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = lastItemIndex ?: 0
    )

    LaunchedEffect(lazyGridState, lazyListState, isLinearLayout) {
        val index = when {
            isLinearLayout -> lazyGridState.firstVisibleItemIndex
            else -> lazyListState.firstVisibleItemIndex
        }
        setLastItemIndex(index)
        if (isLinearLayout) {
            lazyListState.scrollToItem(index)
        } else {
            lazyGridState.scrollToItem(index)
        }
    }
    when (val currentState = characterUiState) {
        is CharacterListUiState.Error -> { /* TODO not implemented yet. */
        }

        CharacterListUiState.Loading -> {
            CustomProgressIndicator()
        }

        is CharacterListUiState.Success -> {
            val characters = currentState.charactersFlow.collectAsLazyPagingItems()

            Scaffold(
                topBar = {
                    CommonTopAppBar(showNavigationIcon = false)
                }
            ) { paddingValues ->

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(Color.Transparent),
                ) {
                    SearchBar(
                        searchQuery = searchQuery,
                        setSearchQuery = setSearchQuery
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
                                }
                            }
                            item {
                                if (characters.loadState.append is LoadState.Loading) {
                                    CustomProgressIndicator(modifier = Modifier.size(50.dp).padding(16.dp))
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
                                        CharacterGridCard(character = character)
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
                        }
                    }
                }
            }
        }
    }
}