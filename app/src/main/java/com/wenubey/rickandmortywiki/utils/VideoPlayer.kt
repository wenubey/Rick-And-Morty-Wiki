package com.wenubey.rickandmortywiki.utils

import android.net.Uri
import android.widget.VideoView
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.wenubey.rickandmortywiki.R

@Composable
fun VideoPlayer(modifier: Modifier = Modifier, @RawRes videoResource: Int) {
    val packageName = LocalContext.current.packageName
    val uri = "android.resource://$packageName/$videoResource"
    var isPlaying by remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        AndroidView(factory = { context ->
            VideoView(context).apply {
                setVideoURI(Uri.parse(uri))
                setMediaController(null)

                setOnPreparedListener { mediaPlayer ->
                    mediaPlayer.isLooping = true
                    if (isPlaying) {
                        start()
                    }
                }

                setOnClickListener {
                    isPlaying = !isPlaying
                    if (isPlaying) {
                        start()
                    } else {
                        pause()
                    }
                }
            }
        })

        Icon(
            imageVector = if (isPlaying) Icons.Filled.PauseCircleFilled else Icons.Filled.PlayCircleFilled,
            contentDescription = stringResource(R.string.cd_play_pause_button),
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp),
            tint = Color.White.copy(alpha = 0.6f)
        )

    }
}
