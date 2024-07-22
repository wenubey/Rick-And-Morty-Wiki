package com.wenubey.rickandmortywiki.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.components.common.CommonTopAppBar
import com.wenubey.rickandmortywiki.ui.components.how_to_use.FeedbackText
import com.wenubey.rickandmortywiki.ui.components.how_to_use.VideoSectionDivider
import com.wenubey.rickandmortywiki.ui.components.how_to_use.VideoSectionHorizontal
import com.wenubey.rickandmortywiki.ui.components.how_to_use.VideoSectionVertical


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowToUseScreen(
    onNavigateBack: () -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {
                CommonTopAppBar(
                    onBackButtonPressed = onNavigateBack,
                    showNavigationIcon = true,
                    title = stringResource(id = R.string.how_to_use_header),
                )
            },
        )
    }) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {

            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    VideoSectionVertical(
                        videoRes = R.raw.double_click_behavior,
                        descriptionRes = R.string.how_to_use_part_one
                    )
                }
                item { VideoSectionDivider() }
                item {
                    VideoSectionVertical(videoRes = R.raw.create_widget, descriptionRes = R.string.how_to_use_part_two)
                }
                item { VideoSectionDivider() }
                item {
                    VideoSectionHorizontal(videoRes = R.raw.widget_size, descriptionRes = R.string.how_to_use_part_three)
                }
                item { VideoSectionDivider() }
            }
            FeedbackText()
        }

    }
}


