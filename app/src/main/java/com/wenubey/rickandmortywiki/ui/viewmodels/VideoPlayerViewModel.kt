package com.wenubey.rickandmortywiki.ui.viewmodels

import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.wenubey.data.ExoPlayerProvider
import com.wenubey.domain.repository.VideoPlayerRepository
import com.wenubey.rickandmortywiki.ui.di.IoDispatcher
import com.wenubey.rickandmortywiki.ui.di.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val videoPlayerRepository: VideoPlayerRepository,
    private val exoPlayerProvider: ExoPlayerProvider,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _videoPlayers = MutableStateFlow<Map<Int, VideoPlayerState>>(emptyMap())
    val videoPlayers: StateFlow<Map<Int, VideoPlayerState>> = _videoPlayers


    fun onVideoReady(@RawRes videoResource: Int) = viewModelScope.launch(ioDispatcher) {
        if (!_videoPlayers.value.containsKey(videoResource)) {
            val exoPlayer = exoPlayerProvider.createPlayer()
            val videoUri = videoPlayerRepository.getVideoUri(videoResource)
            exoPlayer.setMediaItem(MediaItem.fromUri(videoUri))
            exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
            exoPlayer.prepare()
            exoPlayer.playWhenReady = false
            withContext(mainDispatcher) {
                _videoPlayers.value += (videoResource to VideoPlayerState(exoPlayer, false))
            }
        }
    }

    fun onPlayPauseToggle(@RawRes videoResource: Int) = viewModelScope.launch(mainDispatcher) {
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
        viewModelScope.launch(mainDispatcher) {
            _videoPlayers.value.values.forEach { it.exoPlayer.release() }
        }
    }

    data class VideoPlayerState(val exoPlayer: ExoPlayer, val isPlaying: Boolean)
}