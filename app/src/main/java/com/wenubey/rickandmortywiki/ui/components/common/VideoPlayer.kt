package com.wenubey.rickandmortywiki.ui.components.common

import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.viewmodels.VideoPlayerViewModel

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(modifier: Modifier = Modifier, @RawRes videoResource: Int) {
    val viewModel: VideoPlayerViewModel = hiltViewModel()
    val videoPlayerState by viewModel.videoPlayers.collectAsState()
    val state = videoPlayerState[videoResource]
    val interactionSource = remember { MutableInteractionSource() }

    var isPlaying by remember { mutableStateOf(state?.isPlaying ?: false) }

    LaunchedEffect(key1 = videoResource) {
        viewModel.onVideoReady(videoResource)
    }

    Box {
        state?.exoPlayer?.let { exoPlayer ->
            AndroidView(
                modifier = modifier
                    .clickable(
                        indication = null, interactionSource = interactionSource,
                        onClick = {
                            viewModel.onPlayPauseToggle(videoResource)
                            isPlaying = !isPlaying
                        },
                    ),
                factory = { context ->
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                    }
                }
            )
        }

        state?.let {
            var iconAlpha by remember { mutableFloatStateOf(1f) }

            LaunchedEffect(isPlaying) {
                animate(
                    initialValue = iconAlpha,
                    targetValue = if (isPlaying) 0f else 1f,
                    animationSpec = tween(500)
                ) { value, _ ->
                    iconAlpha = value
                }
            }

            Icon(
                imageVector = if (isPlaying) Icons.Filled.PauseCircleFilled else Icons.Filled.PlayCircleFilled,
                contentDescription = stringResource(R.string.cd_play_pause_button),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp)
                    .clickable {
                        viewModel.onPlayPauseToggle(videoResource)
                        isPlaying = !isPlaying
                    },
                tint = Color.White.copy(alpha = iconAlpha)
            )
        }
    }
}
