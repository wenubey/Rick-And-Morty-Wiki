package com.wenubey.rickandmortywiki.ui.nav

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.wenubey.rickandmortywiki.ui.screens.CharacterDetailScreen
import com.wenubey.rickandmortywiki.ui.screens.CharacterEpisodeScreen
import com.wenubey.rickandmortywiki.ui.screens.CharacterListScreen
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterDetailViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterListViewModel
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPreferencesViewModel

fun NavGraphBuilder.characterNavGraph(navController: NavController) {
    navigation(
        route = Graph.CHARACTER,
        startDestination = CharacterScreen.LIST
    ) {
        characterListScreen(navController)
        composable(route = CharacterScreen.DETAIL,
            arguments = listOf(
                navArgument("characterId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val characterId: Int =
                backStackEntry.arguments?.getInt("characterId") ?: -1

            val characterViewModel: CharacterDetailViewModel = hiltViewModel()

            val userPrefViewModel: UserPreferencesViewModel = hiltViewModel()
            val characterUiState = characterViewModel.characterDetailUiState.collectAsState().value
            val userPrefUiState =
                userPrefViewModel.userPreferencesUserPrefUiState.collectAsState().value
            LaunchedEffect(Unit) {
                characterViewModel.getCharacter(characterId)
            }

            CharacterDetailScreen(
                characterUiState = characterUiState,
                userPrefUiState = userPrefUiState,
                onBackButtonPressed = { navController.navigateUp() }
            )
        }
        composable(route = CharacterScreen.EPISODE) {
            CharacterEpisodeScreen()
        }
    }
}

fun NavGraphBuilder.characterListScreen(navController: NavController) {
    composable(route = CharacterScreen.LIST) {
        val userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()
        val userPrefState =
            userPreferencesViewModel.userPreferencesUserPrefUiState.collectAsState().value
        val characterViewModel: CharacterListViewModel = hiltViewModel()
        val characterUiState = characterViewModel.characterListUiState.collectAsState()
        val lastItemIndex = userPreferencesViewModel.lastItemIndex.collectAsState().value

        val searchQuery = characterViewModel.searchQuery.collectAsState().value
        val active = characterViewModel.isSearching.collectAsState().value
        val searchHistory = userPrefState.searchHistory.searchHistory

        CharacterListScreen(
            onCharacterSelected = { characterId ->
                navController.navigateToCharacterDetail(characterId.toString())
            },
            isLinearLayout = userPrefState.linearLayout.isLinearLayout,
            characterUiState = characterUiState.value,
            setLastItemIndex = { index ->
                userPreferencesViewModel.setLastItemIndex(index)
            },
            lastItemIndex = lastItemIndex,
            searchQuery = searchQuery,
            setSearchQuery = characterViewModel::setSearchQuery,
            active = active,
            onActiveChange = characterViewModel::onActiveChange,
            onSearch = characterViewModel::onSearch,
            searchHistory = searchHistory
        )
    }
}




fun NavController.navigateToCharacterDetail(characterId: String) {
    val newRoute = CharacterScreen.DETAIL.replaceAfter("/", characterId)
    this.navigate(newRoute)
}

object CharacterScreen {
    const val LIST = "character_list"
    const val DETAIL = "character_detail/{characterId}"
    const val EPISODE = "character_episode"
}