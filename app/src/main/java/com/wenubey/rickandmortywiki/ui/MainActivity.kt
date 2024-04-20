package com.wenubey.rickandmortywiki.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.wenubey.rickandmortywiki.ui.nav.RootNavigationGraph
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val viewModel: UserPreferencesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController()


            val userPrefUiState = viewModel.userPreferencesUiState.collectAsState().value
            val isScreenLocked = userPrefUiState.screenLock.isScreenLocked
            val isSystemInDarkMode = userPrefUiState.nightMode.isNightMode

            setScreenOrientationMode(isScreenLocked)

            RickAndMortyWikiTheme(darkTheme = isSystemInDarkMode) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootNavigationGraph(navController = navController)
                }
            }
        }
    }

    private fun setScreenOrientationMode(screenOrientationLocked: Boolean) {
        if (screenOrientationLocked) {
            setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED)
        } else {
            setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        }
    }

    private fun setScreenOrientation(screenOrientation: Int) {
        requestedOrientation = screenOrientation
    }
}
