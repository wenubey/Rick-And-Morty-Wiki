package com.wenubey.rickandmortywiki.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.wenubey.rickandmortywiki.ui.screens.location.LocationDetailScreen

fun NavGraphBuilder.locationNavGraph(navController: NavController) {
    navigation(
        route = Graph.LOCATION,
        startDestination = LocationScreen.DETAIL
    ) {
        locationDetailScreen(navController)
    }
}



fun NavGraphBuilder.locationDetailScreen(navController: NavController) {
    composable(route = LocationScreen.DETAIL,
        arguments = listOf(
            navArgument("locationId") {
                type = NavType.IntType
            }
        )
    ) { backStackEntry ->
        val locationId: Int = backStackEntry.arguments?.getInt("locationId") ?: -1
        LocationDetailScreen(
            locationId = locationId,
            onBackButtonPressed = {
                navController.popBackStack()
            },
            onCharacterSelected = { characterId ->
                navController.navigateToCharacterDetail(characterId)
            }
        )
    }
}

fun NavController.navigateToLocationDetail(locationId: Int) {
    val newRoute = LocationScreen.DETAIL.replaceAfter("/", locationId.toString())
    this.navigate(newRoute)
}

object LocationScreen {
    const val DETAIL = "location_detail/{locationId}"
}