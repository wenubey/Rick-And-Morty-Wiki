package com.wenubey.domain.repository

import android.net.Uri
import androidx.annotation.RawRes

interface VideoPlayerRepository {
    fun getVideoUri(@RawRes videoResource: Int): Uri
}