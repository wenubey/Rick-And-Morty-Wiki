package com.wenubey.rickandmortywiki.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wenubey.rickandmortywiki.ui.screens.CharacterDetailScreen
import com.wenubey.rickandmortywiki.ui.screens.CharacterEpisodeScreen
import com.wenubey.rickandmortywiki.ui.screens.CharacterListScreen

fun NavGraphBuilder.characterNavGraph(navController: NavController) {
    navigation(
        route = Graph.CHARACTER,
        startDestination = CharacterScreen.LIST
    ) {
        composable(route = CharacterScreen.LIST) {
            CharacterListScreen(
                onCharacterSelected = { /* TODO not implemented yet. */}
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