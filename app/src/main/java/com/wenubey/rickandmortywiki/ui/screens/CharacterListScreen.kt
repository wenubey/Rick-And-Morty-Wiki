package com.wenubey.rickandmortywiki.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wenubey.rickandmortywiki.ui.nav.CharacterScreen

@Composable
fun CharacterListScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = CharacterScreen.LIST)
    }
}