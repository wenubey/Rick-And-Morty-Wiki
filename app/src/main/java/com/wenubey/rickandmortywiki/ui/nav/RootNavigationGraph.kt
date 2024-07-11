package com.wenubey.rickandmortywiki.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun RootNavigationGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, route = Graph.ROOT, startDestination = Graph.TAB) {
        tabNavGraph(navController)
        characterNavGraph(navController)
        locationNavGraph(navController)
        settingsNavGraph(navController)
    }
}


object Graph {
    const val ROOT = "root_graph"
    const val TAB = "tab_graph"
    const val CHARACTER = "character_graph"
    const val LOCATION = "location_graph"
    const val SETTINGS = "settings_graph"
}