package com.wenubey.rickandmortywiki.ui.components.location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wenubey.domain.model.Character
import com.wenubey.rickandmortywiki.ui.components.character.list.CharacterGridCard

@Composable
fun LocationDetailResidentsComponents(
    residents: List<Character>,
    onCharacterSelected: (Int) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(4.dp),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            residents
        ) { resident ->
            CharacterGridCard(
                character = resident,
                onCardClick = { onCharacterSelected(resident.id) }
            )
        }
    }
}