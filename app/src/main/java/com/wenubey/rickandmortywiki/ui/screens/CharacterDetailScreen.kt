package com.wenubey.rickandmortywiki.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wenubey.rickandmortywiki.ui.components.character.detail.CharacterPropertiesComponent
import com.wenubey.rickandmortywiki.ui.components.character.detail.CharacterStatusDetailComponent
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.common.CustomProgressIndicator
import com.wenubey.rickandmortywiki.ui.viewmodels.CharacterDetailUiState
import com.wenubey.rickandmortywiki.ui.viewmodels.UserPrefUiState

@Composable
internal fun CharacterDetailScreen(
    characterUiState: CharacterDetailUiState,
    userPrefUiState: UserPrefUiState,
    onBackButtonPressed: () -> Unit,
    onEpisodeClicked: (id: Int) -> Unit,
) = when (val currentState = characterUiState) {
    is CharacterDetailUiState.Error -> { /* TODO( not yet implemented.) */
    }

    is CharacterDetailUiState.Loading -> {
        CustomProgressIndicator()
    }

    is CharacterDetailUiState.Success -> {
        val character = currentState.character

        Scaffold(
            topBar = {
                CommonTopAppBar(
                    showNavigationIcon = true,
                    title = character.name,
                    onBackButtonPressed = onBackButtonPressed,
                )
            }
        ) { paddingValues ->


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    CharacterStatusDetailComponent(
                        characterStatus = character.status,
                        imageUrl = character.imageUrl
                    )
                }
                
                item { 
                    CharacterPropertiesComponent(character = character)
                }

//                item {
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        OutlinedCard(
//                            modifier = Modifier
//                                .weight(0.5f),
//                            border = BorderStroke(width = 1.dp, color = Color.Magenta),
//
//                        ) {
//                            DataPointComponent(
//                                dataPoint = DataPoint("Gender", character.gender.displayName),
//                                modifier = Modifier
//                                    .padding(vertical = 8.dp, horizontal = 4.dp)
//                                    .fillMaxWidth()
//                            )
//                        }
//                        OutlinedCard(
//                            modifier = Modifier
//                                .weight(0.5f),
//                            border = BorderStroke(width = 1.dp, color = Color.Magenta)
//                        ) {
//                            DataPointComponent(
//                                dataPoint = DataPoint(
//                                    "Episode Count",
//                                    character.episodeIds.size.toString()
//                                ), modifier = Modifier
//                                    .padding(vertical = 8.dp, horizontal = 4.dp)
//                                    .fillMaxWidth()
//                            )
//                        }
//                    }
//                }

//                item {
//                    Spacer(modifier = Modifier.height(8.dp))
//                    OutlinedCard(
//                        modifier = Modifier.fillMaxWidth(),
//                        border = BorderStroke(width = 1.dp, color = Color.Magenta)
//                    ) {
//                        DataPointComponent(
//                            dataPoint = DataPoint("Last known location", character.location.name),
//                            modifier = Modifier
//                                .padding(vertical = 8.dp, horizontal = 4.dp)
//                                .fillMaxWidth()
//                        )
//                    }
//                }
//
//                item {
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                        OutlinedCard(
//                            modifier = Modifier.fillMaxWidth(),
//                            border = BorderStroke(width = 1.dp, color = Color.Magenta)
//                        ) {
//                            DataPointComponent(
//                                dataPoint = DataPoint("Species", character.species),
//                                modifier = Modifier
//                                    .align(Alignment.CenterHorizontally)
//                                    .padding(vertical = 8.dp, horizontal = 4.dp)
//                            )
//                        }
//                    }
//
//                }


//                items(dataPoints) { dataPoint ->
//                    Spacer(modifier = Modifier.height(16.dp))
//                    DataPointComponent(dataPoint = dataPoint)
//                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    ShowAllEpisodeButton(
                        onEpisodeClicked = {
                            onEpisodeClicked(character.id)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ShowAllEpisodeButton(onEpisodeClicked: () -> Unit) {
    OutlinedButton(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        onClick = onEpisodeClicked,
        border = BorderStroke(width = 1.dp, color = Color.Magenta),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = "View all episodes", fontSize = 16.sp, textAlign = TextAlign.Center)
    }
}



