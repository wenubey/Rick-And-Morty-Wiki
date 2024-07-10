package com.wenubey.rickandmortywiki.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wenubey.rickandmortywiki.ui.components.common.CopyrightScreen
import com.wenubey.rickandmortywiki.ui.screens.HowToUseScreen


fun NavGraphBuilder.settingsNavGraph(navController: NavController) {
    navigation(
        route = Graph.SETTINGS,
        startDestination = SettingsScreens.COPYRIGHT
    ) {
        copyrightScreen(navController)
        howToUseScreen(navController)
    }
}

fun NavGraphBuilder.copyrightScreen(navController: NavController) {
    composable(route = SettingsScreens.COPYRIGHT,
    ) {
        CopyrightScreen(
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }
}
fun NavGraphBuilder.howToUseScreen(navController: NavController) {
    composable(route = SettingsScreens.HOW_TO_USE,
    ) {
        HowToUseScreen(
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }
}


object SettingsScreens {
    const val COPYRIGHT = "copyright_screen"
    const val HOW_TO_USE = "how_to_use_screen"
}