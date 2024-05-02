package com.wenubey.rickandmortywiki.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.wenubey.rickandmortywiki.ui.screens.CharacterDetailScreen
import com.wenubey.rickandmortywiki.ui.screens.CharacterListScreen

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


            CharacterDetailScreen(
                characterId = characterId,
                onBackButtonPressed = { navController.navigateUp() },
                navigateToLocationScreen = {
                    // TODO add navigation to location list view
                }
            )
        }
    }
}

fun NavGraphBuilder.characterListScreen(navController: NavController) {
    composable(route = CharacterScreen.LIST) {

        CharacterListScreen(
            onCharacterSelected = { characterId ->
                navController.navigateToCharacterDetail(characterId.toString())
            },
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