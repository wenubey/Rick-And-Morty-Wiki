package com.wenubey.rickandmortywiki.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.wenubey.rickandmortywiki.utils.HomeTabs
import com.wenubey.rickandmortywiki.ui.screens.character.CharacterDetailScreen

fun NavGraphBuilder.characterNavGraph(navController: NavController) {
    navigation(
        route = Graph.CHARACTER,
        startDestination = CharacterScreen.DETAIL
    ) {
        characterDetailScreen(navController)
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
                navController.navigateToTabScreen(HomeTabs.Location.ordinal)
            }
        )
    }
}




fun NavController.navigateToCharacterDetail(characterId: Int) {
    val newRoute = CharacterScreen.DETAIL.replaceAfter("/", characterId.toString())
    this.navigate(newRoute)
}

object CharacterScreen {
    const val DETAIL = "character_detail/{characterId}"
}