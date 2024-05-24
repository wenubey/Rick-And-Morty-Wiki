package com.wenubey.rickandmortywiki.ui.nav

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.wenubey.rickandmortywiki.ui.screens.TabScreen
import com.wenubey.rickandmortywiki.ui.viewmodels.user_pref.UserPreferencesViewModel

fun NavGraphBuilder.tabNavGraph(navController: NavController) {
    navigation(
        route = Graph.TAB,
        startDestination = TabScreen.TAB_SCREEN
    ) {
        composable(
            route = TabScreen.TAB_SCREEN,
            arguments = listOf(
                navArgument("tabIndex") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val tabIndex: Int =
                backStackEntry.arguments?.getInt("tabIndex") ?: 0

            val userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()
            TabScreen(
                tabIndex = tabIndex,
                onCharacterSelected = { navController.navigateToCharacterDetail(it) },
                onLocationSelected = { navController.navigateToLocationDetail(it) },
                navigateUp = { navController.popBackStack() },
                userPreferencesViewModel = userPreferencesViewModel,
                events = userPreferencesViewModel,
            )
        }
    }
}

fun NavController.navigateToTabScreen(tabIndex: Int?) {
    this.navigate(TabScreen.TAB_SCREEN.replaceAfter("/", tabIndex?.toString() ?: "0"))
}


object TabScreen {
    const val TAB_SCREEN = "tab_screen/{tabIndex}"
}
