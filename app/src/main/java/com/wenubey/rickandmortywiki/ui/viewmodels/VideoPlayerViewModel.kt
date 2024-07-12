package com.wenubey.rickandmortywiki.ui.viewmodels

import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.wenubey.data.ExoPlayerProvider
import com.wenubey.domain.repository.VideoPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val videoPlayerRepository: VideoPlayerRepository,
    private val exoPlayerProvider: ExoPlayerProvider,
) : ViewModel() {

    private val _videoPlayers = MutableStateFlow<Map<Int, VideoPlayerState>>(emptyMap())
    val videoPlayers: StateFlow<Map<Int, VideoPlayerState>> = _videoPlayers


    fun onVideoReady(@RawRes videoResource: Int) {
        if (!_videoPlayers.value.containsKey(videoResource)) {
            val exoPlayer = exoPlayerProvider.createPlayer()
            val videoUri = videoPlayerRepository.getVideoUri(videoResource)
            exoPlayer.setMediaItem(MediaItem.fromUri(videoUri))
            exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
            _videoPlayers.value += (videoResource to VideoPlayerState(exoPlayer, true))
        }
    }

    fun onPlayPauseToggle(@RawRes videoResource: Int) {
        _videoPlayers.value = _videoPlayers.value.mapValues { (key, state) ->
            if (key == videoResource) {
                val newIsPlaying = !state.isPlaying
                if (newIsPlaying) {
                    state.exoPlayer.play()
                } else {
                    state.exoPlayer.pause()
                }
                state.copy(isPlaying = newIsPlaying)
            } else {
                state
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _videoPlayers.value.values.forEach { it.exoPlayer.release() }
    }

    data class VideoPlayerState(val exoPlayer: ExoPlayer, val isPlaying: Boolean)
}