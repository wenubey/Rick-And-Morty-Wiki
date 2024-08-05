package com.wenubey.data

import android.util.Log
import com.wenubey.data.util.ApiUtil.handleRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RickAndMortyApiClientTest {

    private lateinit var rickAndMortyApiClient: RickAndMortyApiClient
    private val mockEngine = MockEngine { request ->
        handleRequest(request)
    }
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        val httpClient = setupHttpClient()

        rickAndMortyApiClient =
            RickAndMortyApiClient(
                ioDispatcher = testDispatcher,
                httpClient = httpClient
            )


        mockkStatic(Log::class)

        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @Test
    fun `get character success`() = runTest {
        // Given
        val result = rickAndMortyApiClient.getCharacter(1)

        // When
        val character = result.getOrNull()

        // Then
        assert(character != null)
        assert(character?.name == "Rick Sanchez")
    }

    @Test
    fun `get characterPage success`() = runTest {
        // Given
        val result = rickAndMortyApiClient.getCharacterPage(1)

        // When
        val characterPage = result.getOrNull()

        // Then
        assert(characterPage != null)
        assert(characterPage?.info?.count == 826)
        verify { Log.d("RickAndMortyApiClient", "safeApiCall:SUCCESS") }
    }

    @Test
    fun `get search character success`() = runTest {
        // Given
        val result = rickAndMortyApiClient.searchCharacter(1, "Rick Sanchez")

        // When
        val characterPage = result.getOrNull()

        // Then
        assert(characterPage != null)
        assert(characterPage?.results?.size == 4)
        assert(characterPage?.info?.count == 4)
    }

    @Test
    fun `get characters success`() = runTest {
        // Given
        val result = rickAndMortyApiClient.getCharacters(listOf(1, 2))

        // When
        val characters = result.getOrNull()

        // Then
        assert(characters != null)
        assert(characters?.size == 2)
        assert(characters?.get(0)?.name == "Rick Sanchez")
        assert(characters?.get(1)?.name == "Morty Smith")
    }

    @Test
    fun `get episode success`() = runTest {
        // Given
        val result = rickAndMortyApiClient.getEpisode(51)

        // When
        val episode = result.getOrNull()

        // Then
        assert(episode != null)
        assert(episode?.name == "Rickmurai Jack")
        assert(episode?.seasonNumber == 5)
        assert(episode?.episodeNumber == 10)
    }

    @Test
    fun `get episodes success`() = runTest {
        // Given
        val result = rickAndMortyApiClient.getEpisodes(listOf(1, 2))

        // When
        val episodes = result.getOrNull()

        // Then
        assert(episodes != null)
        assert(episodes?.size == 2)
        assert(episodes?.get(0)?.name == "Pilot")
        assert(episodes?.get(1)?.name == "Lawnmower Dog")
    }

    @Test
    fun `get location success`() = runTest {
        // Given
        val result = rickAndMortyApiClient.getLocation(1)

        // When
        val location = result.getOrNull()

        // Then
        assert(location != null)
        assert(location?.name == "Earth (C-137)")
    }

    @Test
    fun `get locationPage success`() = runTest {
        // Given
        val result = rickAndMortyApiClient.getLocationPage(1)

        // When
        val locationPage = result.getOrNull()

        // Then
        assert(locationPage != null)
        assert(locationPage?.info?.count == 126)
        assert(locationPage?.results?.size == 20)
        assert(locationPage?.results?.get(0)?.name == "Earth (C-137)")
    }

    @Test
    fun `get search location with parameter success`() = runTest {
        // Given
        val result = rickAndMortyApiClient.searchLocation(1, "Dimension C-137", "dimension")

        // When
        val locationPage = result.getOrNull()

        // Then
        assert(locationPage != null)
    }

    @Test
    fun `get search location without parameter success`() = runTest {
        // Given
        val result = rickAndMortyApiClient.searchLocation(
            1,
            searchParameter = null,
            searchQuery = "Abadango"
        )

        // When
        val locationPage = result.getOrNull()

        // Then
        assert(locationPage != null)
        assert(locationPage?.results?.size == 1)
        assert(locationPage?.results?.get(0)?.name == "Abadango")
    }

    private fun setupHttpClient(): HttpClient = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }
}