package com.wenubey.rickandmortywiki.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wenubey.rickandmortywiki.ui.components.character.CharacterGridCard
import com.wenubey.rickandmortywiki.ui.components.character.CharacterListCard
import com.wenubey.rickandmortywiki.ui.isSystemInPortraitOrientation
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterListUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterListViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPreferencesViewModel

@Composable
fun CharacterListScreen(
    characterViewModel: CharacterListViewModel = hiltViewModel(),
    userPrefViewModel: UserPreferencesViewModel = hiltViewModel(),
    onCharacterSelected: (id: Int) -> Unit,
) {
    val characterUiState by characterViewModel.characterListUiState.collectAsState()
    val userPrefUiState by userPrefViewModel.userPreferencesUiState.collectAsState()

    var isLinearLayout by remember {
        mutableStateOf(userPrefUiState.linearLayout.isLinearLayout)
    }
    LaunchedEffect(key1 = characterViewModel, block = { characterViewModel.getInitialPage() })

    when (val currentState = characterUiState) {
        is CharacterListUiState.Error -> { /* TODO not implemented yet. */ }

        CharacterListUiState.Loading -> { /* TODO not implemented yet. */ }

        is CharacterListUiState.Success -> {
            Surface {
                Scaffold { paddingValues ->
                    if (isLinearLayout) {
                        if (isSystemInPortraitOrientation()) {
                            LazyColumn(
                                modifier = Modifier.padding(paddingValues),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                items(
                                    items = currentState.characters,
                                    key = { it.id },
                                ) { character ->
                                    CharacterListCard(
                                        character = character,
                                        onCharacterSelected = {
                                            onCharacterSelected(character.id)
                                        },
                                    )
                                }
                            }
                        } else {
                            LazyVerticalGrid(
                                modifier = Modifier.padding(paddingValues),
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                items(
                                    items = currentState.characters,
                                    key = { it.id },
                                ) { character ->
                                    CharacterListCard(
                                        character = character,
                                        onCharacterSelected = {
                                            onCharacterSelected(character.id)
                                        },
                                    )
                                }
                            }
                        }
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier.padding(paddingValues),
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(items = currentState.characters, key = { it.id }) { character ->
                                CharacterGridCard(character = character)
                            }
                        }
                    }
                }
            }

        }
    }
}