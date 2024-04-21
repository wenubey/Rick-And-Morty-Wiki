package com.wenubey.rickandmortywiki.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wenubey.rickandmortywiki.ui.components.character.CharacterGridCard
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterListUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterListViewModel

//TODO don't forget to add userPref viewModel for list and grid view changes.
@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val uiState by viewModel.characterListUiState.collectAsState()

    LaunchedEffect(key1 = viewModel, block = { viewModel.getInitialPage() })

    when (val currentState = uiState) {
        is CharacterListUiState.Error -> { /* TODO not implemented yet. */ }
        CharacterListUiState.Loading -> { /* TODO not implemented yet. */ }
        is CharacterListUiState.Success -> {
            Surface {
                Scaffold { paddingValues ->
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