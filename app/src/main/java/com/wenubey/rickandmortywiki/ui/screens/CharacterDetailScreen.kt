package com.wenubey.rickandmortywiki.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.common.CustomProgressIndicator
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterDetailUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPrefUiState

@Composable
internal fun CharacterDetailScreen(
    characterUiState: CharacterDetailUiState,
    userPrefUiState: UserPrefUiState,
    onBackButtonPressed: () -> Unit,
) {
    when (val currentState = characterUiState) {
        is CharacterDetailUiState.Error -> { /* TODO( not yet implemented.) */
        }

        is CharacterDetailUiState.Loading -> {
            CustomProgressIndicator()
        }

        is CharacterDetailUiState.Success -> {
            val character = currentState.character
            val dataPoints = currentState.dataPoints

            Scaffold(
                topBar = {
                    CommonTopAppBar(
                        showNavigationIcon = true,
                        title = character.name,
                        onBackButtonPressed = onBackButtonPressed,
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                ) {
                    Text(text = character.name)
                }
            }
        }
    }
}