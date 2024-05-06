package com.wenubey.rickandmortywiki.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.wenubey.rickandmortywiki.ui.screens.location.LocationDetailScreen
import com.wenubey.rickandmortywiki.ui.screens.location.LocationListScreen

fun NavGraphBuilder.locationNavGraph(navController: NavController) {
    navigation(
        route = Graph.LOCATION,
        startDestination = LocationScreen.LIST
    ) {
        locationListScreen(navController)
        locationDetailScreen(navController)
    }
}

fun NavGraphBuilder.locationListScreen(navController: NavController) {
    composable(route = LocationScreen.LIST) {
        LocationListScreen(
            onLocationSelected = { locationId ->
                navController.navigateToLocationDetail(locationId.toString())
            },
            navigateUp = {
                navController.popBackStack()
            }
        )
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
                navController.navigateToCharacterDetail(characterId.toString())
            }
        )
    }
}

fun NavController.navigateToLocationDetail(locationId: String) {
    val newRoute = LocationScreen.DETAIL.replaceAfter("/", locationId)
    this.navigate(newRoute)
}

object LocationScreen {
    const val LIST = "location_list"
    const val DETAIL = "location_detail/{locationId}"
}