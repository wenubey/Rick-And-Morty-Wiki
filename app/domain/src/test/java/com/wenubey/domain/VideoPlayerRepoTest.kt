package com.wenubey.domain

import android.net.Uri
import androidx.annotation.RawRes
import com.wenubey.domain.repository.VideoPlayerRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class VideoPlayerManager(private val videoPlayerRepository: VideoPlayerRepository) {

    fun getVideoUriForResource(@RawRes videoResource: Int): Uri {
        return videoPlayerRepository.getVideoUri(videoResource)
    }
}

class VideoPlayerRepoTest {

    private lateinit var videoPlayerRepository: VideoPlayerRepository
    private lateinit var videoPlayerManager: VideoPlayerManager

    @Before
    fun setup() {
        videoPlayerRepository = mockk()
        videoPlayerManager = VideoPlayerManager(videoPlayerRepository)

        mockkStatic(Uri::class)
    }

    @After
    fun teardown() {
        unmockkStatic(Uri::class)
    }

    @Test
    fun `getVideoUriForResource returns correct Uri from repository`() {
        // Given
        val videoResourceId = 123
        val expectedUri = mockk<Uri>()

        every { Uri.parse("android.resource://com.example.app/$videoResourceId") } returns expectedUri

        every { videoPlayerRepository.getVideoUri(videoResourceId) } returns expectedUri

        // When
        val actualUri = videoPlayerManager.getVideoUriForResource(videoResourceId)

        // Then
        assertEquals(expectedUri, actualUri)
        verify { videoPlayerRepository.getVideoUri(videoResourceId) }
    }

    @Test
    fun `getVideoUriForResource throws RuntimeException when Uri parsing fails`() {
        // Given
        val videoResourceId = 123

        every { Uri.parse("android.resource://com.example.app/$videoResourceId") } throws RuntimeException("Failed to parse URI")
        every { videoPlayerRepository.getVideoUri(videoResourceId) } throws RuntimeException("Failed to parse URI")

        // Then
        assertThrows(RuntimeException::class.java) {
            videoPlayerManager.getVideoUriForResource(videoResourceId)
        }
        verify { videoPlayerRepository.getVideoUri(videoResourceId) }
    }

}

