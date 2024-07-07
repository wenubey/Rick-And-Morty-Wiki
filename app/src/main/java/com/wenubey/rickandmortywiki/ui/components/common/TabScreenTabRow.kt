package com.wenubey.rickandmortywiki.ui.components.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.wenubey.rickandmortywiki.utils.HomeTabs
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabScreenTabRow(
    pagerState: PagerState,
    currentTabIndex: Int
) {

    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = currentTabIndex,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                // TODO change color when color palette created.
                color = Color.Magenta,
                modifier = Modifier
                    .fillMaxWidth()
                    .tabIndicatorOffset(tabPositions[currentTabIndex])
            )
        }

    ) {
        HomeTabs.entries.forEachIndexed { index, tab ->
            val isSelected = currentTabIndex == index
            Tab(
                selected = isSelected,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(tab.ordinal)
                    }
                },
            ) {
                Icon(
                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                    contentDescription = stringResource(id = tab.text)
                )
                Text(text = stringResource(id = tab.text))
            }
        }
    }
}