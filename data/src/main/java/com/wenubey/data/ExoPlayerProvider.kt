package com.wenubey.data

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExoPlayerProvider @Inject constructor(private val context: Context) {
    fun createPlayer(): ExoPlayer = ExoPlayer.Builder(context).build()
}