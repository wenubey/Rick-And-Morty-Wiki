package com.wenubey.rickandmortywiki.ui.components.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.isSystemInPortraitOrientation
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
    val searchBarModifier =
        if (!isSystemInPortraitOrientation() && active) Modifier.fillMaxHeight(0.4f) else Modifier
    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .then(searchBarModifier),
        query = searchQuery,
        onQueryChange = setSearchQuery,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        placeholder = { Text(text = stringResource(id = R.string.search_bar_label)) },
        colors = SearchBarDefaults.colors(
            dividerColor = Color.Magenta,
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        SearchBarHistoryList(
            searchHistory = searchHistory,
            active = active,
            onSearch = onSearch
        )
    }
}

@Composable
fun SearchBarHistoryList(
    searchHistory: List<String> = listOf(),
    active: Boolean = false,
    onSearch: (String) -> Unit,
) {
    val historyItemListHeight = if (!isSystemInPortraitOrientation()) {
        if (active) 1f else 0.1f
    } else {
        if (active) 0.6f else 0.1f
    }
    if (isSystemInPortraitOrientation()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(historyItemListHeight),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            items(searchHistory) { searchItem ->
                SearchBarHistoryCard(
                    onClick = {
                        onSearch(searchItem)
                    },
                    searchItem = searchItem,
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(searchHistory) { searchItem ->
                SearchBarHistoryCard(
                    onClick = {
                        onSearch(searchItem)
                    },
                    searchItem = searchItem,
                )
            }
        }
    }
}

@Composable
fun SearchBarHistoryCard(
    onClick: () -> Unit,
    searchItem: String
) {
    Card(
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = stringResource(R.string.cd_search_history_item),
            )
            Text(
                modifier = Modifier, text = searchItem
            )
        }
    }
}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun SearchBarPreview() {
    RickAndMortyWikiTheme {
        Surface {
            CustomSearchBar(
                active = true,
                searchHistory = listOf(
                    "Search1",
                    "Search2",
                    "Search3",
                    "Search4",
                )
            )
        }
    }
}

