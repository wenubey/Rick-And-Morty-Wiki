package com.wenubey.rickandmortywiki.ui.nav

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wenubey.rickandmortywiki.ui.screens.CharacterDetailScreen
import com.wenubey.rickandmortywiki.ui.screens.CharacterEpisodeScreen
import com.wenubey.rickandmortywiki.ui.screens.CharacterListScreen
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterListViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPreferencesViewModel

fun NavGraphBuilder.characterNavGraph(navController: NavController) {
    navigation(
        route = Graph.CHARACTER,
        startDestination = CharacterScreen.LIST
    ) {
        composable(route = CharacterScreen.LIST) {
            val userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()
            val userPrefState = userPreferencesViewModel.userPreferencesUiState.collectAsState().value
            val characterViewModel: CharacterListViewModel = hiltViewModel()
            val characterUiState = characterViewModel.characterListUiState.collectAsState()
            val lastItemIndex = userPreferencesViewModel.lastItemIndex.collectAsState().value
            val searchQuery = characterViewModel.searchQuery.collectAsState().value

            CharacterListScreen(
                onCharacterSelected = { /* TODO not implemented yet. */},
                isLinearLayout = userPrefState.linearLayout.isLinearLayout,
                characterUiState = characterUiState.value,
                setLastItemIndex = { index ->
                    userPreferencesViewModel.setLastItemIndex(index)
                },
                lastItemIndex = lastItemIndex,
                searchQuery = searchQuery,
                setSearchQuery = characterViewModel::setSearchQuery
            )
        }
        composable(route = CharacterScreen.DETAIL) {
            CharacterDetailScreen()
        }
        composable(route = CharacterScreen.EPISODE) {
            CharacterEpisodeScreen()
        }
    }
}

object CharacterScreen {
    const val LIST = "character_list"
    const val DETAIL = "character_detail"
    const val EPISODE = "character_episode"
}