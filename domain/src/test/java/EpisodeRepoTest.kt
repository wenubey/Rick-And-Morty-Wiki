import com.wenubey.domain.model.Episode
import com.wenubey.domain.repository.EpisodeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EpisodeRepoTest {

    private val episodeRepository: EpisodeRepository = mockk()

    @Test
    fun `test getEpisode success`() = runTest {
        // Given
        val episode = singleEpisode
        coEvery { episodeRepository.getEpisode(1) } returns Result.success(episode)

        // When
        val result = episodeRepository.getEpisode(1)

        // Then
        assert(result.isSuccess)
        assertEquals(episode, result.getOrNull())
        coVerify { episodeRepository.getEpisode(1) }
    }

    @Test
    fun `test getEpisode failure`() = runTest {
        // Given
        val exception = RuntimeException("Episode not found")
        coEvery { episodeRepository.getEpisode(1) } returns Result.failure(exception)

        // When
        val result = episodeRepository.getEpisode(1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify { episodeRepository.getEpisode(1) }
    }

    @Test
    fun `test getEpisodes success`() = runTest {
        // Arrange
        val episodes = multipleEpisodes
        coEvery { episodeRepository.getEpisodes(listOf(1, 2, 3)) } returns Result.success(episodes)

        // Act
        val result = episodeRepository.getEpisodes(listOf(1, 2, 3))

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(episodes, result.getOrNull())
        coVerify { episodeRepository.getEpisodes(listOf(1, 2, 3)) }
    }

    @Test
    fun `test getEpisodes failure`() = runTest {
        // Arrange
        val exception = RuntimeException("Episodes not found")
        coEvery { episodeRepository.getEpisodes(listOf(1, 2, 3)) } returns Result.failure(exception)

        // Act
        val result = episodeRepository.getEpisodes(listOf(1, 2, 3))

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify { episodeRepository.getEpisodes(listOf(1, 2, 3)) }
    }

    companion object {
        private val singleEpisode = Episode(
            id = 1,
            name = "Pilot",
            airDate = "December 2, 2013",
            seasonNumber = 1,
            episodeNumber = 1,
            charactersInEpisode = listOf(),
        )
        private val multipleEpisodes = (1..3).map {
            Episode(
                id = it,
                name = "Pilot $it",
                airDate = "December $it, 2013",
                seasonNumber = it + 2,
                episodeNumber = it + 1,
                charactersInEpisode = listOf(),
            )
        }
    }
}