package com.wenubey.rickandmortywiki.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.ui.components.character.detail.CharacterPropertiesComponent
import com.wenubey.rickandmortywiki.ui.components.character.detail.CharacterStatusDetailComponent
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.common.CustomProgressIndicator
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterDetailUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPrefUiState

@Composable
internal fun CharacterDetailScreen(
    characterUiState: CharacterDetailUiState,
    userPrefUiState: UserPrefUiState,
    onBackButtonPressed: () -> Unit,
    onEpisodeClicked: (id: Int) -> Unit,
) = when (val currentState = characterUiState) {
    is CharacterDetailUiState.Error -> {
        Text(text = currentState.message)
    }

    is CharacterDetailUiState.Loading -> {
        CustomProgressIndicator()
    }

    is CharacterDetailUiState.Success -> {
        val character = currentState.character

        Scaffold(
            topBar = {
                CommonTopAppBar(
                    showNavigationIcon = true,
                    title = character.name,
                    onBackButtonPressed = onBackButtonPressed,
                )
            }
        ) { paddingValues ->


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    CharacterStatusDetailComponent(
                        characterStatus = character.status,
                        imageUrl = character.imageUrl
                    )
                }
                
                item { 
                    CharacterPropertiesComponent(character = character)
                }


            }
        }
    }
}



