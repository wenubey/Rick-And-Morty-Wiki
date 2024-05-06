package com.wenubey.rickandmortywiki.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.wenubey.rickandmortywiki.ui.screens.character.CharacterDetailScreen
import com.wenubey.rickandmortywiki.ui.screens.character.CharacterListScreen

fun NavGraphBuilder.characterNavGraph(navController: NavController) {
    navigation(
        route = Graph.CHARACTER,
        startDestination = CharacterScreen.LIST
    ) {
        characterListScreen(navController)
        characterDetailScreen(navController)
    }
}

fun NavGraphBuilder.characterListScreen(navController: NavController) {
    composable(route = CharacterScreen.LIST) {
        CharacterListScreen(
            onCharacterSelected = { characterId ->
                navController.navigateToCharacterDetail(characterId.toString())
            },
            navigateUp = {
                navController.popBackStack()
            }
        )
    }
}

fun NavGraphBuilder.characterDetailScreen(navController: NavController) {
    composable(route = CharacterScreen.DETAIL,
        arguments = listOf(
            navArgument("characterId") {
                type = NavType.IntType
            }
        )
    ) { backStackEntry ->
        val characterId: Int =
            backStackEntry.arguments?.getInt("characterId") ?: -1


        CharacterDetailScreen(
            characterId = characterId,
            onBackButtonPressed = { navController.navigateUp() },
            navigateToLocationScreen = {
                navController.navigate(Graph.LOCATION)

            }
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
}