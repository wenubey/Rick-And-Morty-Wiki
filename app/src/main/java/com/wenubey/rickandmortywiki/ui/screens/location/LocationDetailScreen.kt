package com.wenubey.rickandmortywiki.ui.screens.location

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.wenubey.rickandmortywiki.ui.nav.LocationScreen

@Composable
fun LocationDetailScreen(
    navController: NavController,
    locationId: Int,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = LocationScreen.DETAIL)
    }
}