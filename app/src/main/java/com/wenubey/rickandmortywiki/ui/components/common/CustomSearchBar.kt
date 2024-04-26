package com.wenubey.rickandmortywiki.ui.components.common

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    searchQuery: String = "Preview",
    setSearchQuery: (String) -> Unit = {},
    searchHistory: List<String> = listOf(),
    active: Boolean = false,
    onActiveChange: (Boolean) -> Unit = {},
    onSearch: (String) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column {
        // TODO add place holder
        DockedSearchBar(
            modifier = Modifier
                .fillMaxWidth(),
            query = searchQuery,
            // it change the query
            onQueryChange = setSearchQuery,
            // it invoked the search clicked on the keyboard
            onSearch = onSearch,
            // it open/close the history
            active = active,
            // it change the active
            onActiveChange = onActiveChange,

            ) {

            LazyColumn {
                items(searchHistory) { searchItem ->
                    Text(
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                onSearch(searchItem)
                            },
                        ), text = searchItem
                    )
                }
            }


        }

    }
}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun SearchBarPreview() {
    RickAndMortyWikiTheme {
        Surface {
            CustomSearchBar()
        }
    }
}

