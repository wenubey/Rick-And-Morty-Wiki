package com.wenubey.data.repository

import android.content.Context
import android.net.Uri
import com.wenubey.domain.repository.VideoPlayerRepository
import javax.inject.Inject

class VideoPlayerRepositoryImpl @Inject constructor(
    private val context: Context
) : VideoPlayerRepository {
    override fun getVideoUri(videoResource: Int): Uri {
        val packageName = context.packageName
        return Uri.parse("$RESOURCE_DIR$packageName/$videoResource")
    }

    private companion object {
        const val RESOURCE_DIR = "android.resource://"
    }
}