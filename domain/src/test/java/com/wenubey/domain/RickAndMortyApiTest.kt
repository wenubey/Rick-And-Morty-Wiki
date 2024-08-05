package com.wenubey.domain

import com.wenubey.domain.model.CharacterGender
import com.wenubey.domain.model.CharacterPage
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Episode
import com.wenubey.domain.model.Location
import com.wenubey.domain.model.LocationPage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RickAndMortyApiTest {

    private lateinit var rickAndMortyApi: RickAndMortyApi

    @Before
    fun setup() {
        rickAndMortyApi = mockk()
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `get character success`() = runTest {
        // Given
        val character = character
        coEvery { rickAndMortyApi.getCharacter(1) } returns Result.success(character)

        // When
        val result = rickAndMortyApi.getCharacter(1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), character)
        coVerify { rickAndMortyApi.getCharacter(1) }
    }

    @Test
    fun `get character failure`() = runTest {
        // Given
        val exception = Exception("Failed to fetch character")
        coEvery { rickAndMortyApi.getCharacter(1) } returns Result.failure(exception)

        // When
        val result = rickAndMortyApi.getCharacter(1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull(), exception)
        coVerify { rickAndMortyApi.getCharacter(1) }
    }

    @Test
    fun `get character page success`() = runTest {
        // Given
        val characterPage = characterPage
        coEvery { rickAndMortyApi.getCharacterPage(1) } returns Result.success(characterPage)

        // When
        val result = rickAndMortyApi.getCharacterPage(1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), characterPage)
        coVerify { rickAndMortyApi.getCharacterPage(1) }
    }

    @Test
    fun `get character page failure`() = runTest {
        // Given
        val exception = Exception("Failed to fetch character page")
        coEvery { rickAndMortyApi.getCharacterPage(1) } returns Result.failure(exception)

        // When
        val result = rickAndMortyApi.getCharacterPage(1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull(), exception)
        coVerify { rickAndMortyApi.getCharacterPage(1) }
    }

    @Test
    fun `search character success`() = runTest {
        // Given
        val characterPage = characterPage
        coEvery { rickAndMortyApi.searchCharacter(1, "Rick Sanchez") } returns Result.success(
            characterPage
        )

        // When
        val result = rickAndMortyApi.searchCharacter(1, "Rick Sanchez")

        // Then
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), characterPage)
        coVerify { rickAndMortyApi.searchCharacter(1, "Rick Sanchez") }
    }

    @Test
    fun `search character failure`() = runTest {
        // Given
        val exception = Exception("Failed to search character")
        coEvery { rickAndMortyApi.searchCharacter(1, "Rick Sanchez") } returns Result.failure(
            exception
        )

        // When
        val result = rickAndMortyApi.searchCharacter(1, "Rick Sanchez")

        // Then
        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull(), exception)
        coVerify { rickAndMortyApi.searchCharacter(1, "Rick Sanchez") }
    }

    @Test
    fun `get characters success`() = runTest {
        // Given
        val characters = characters.take(2)
        coEvery { rickAndMortyApi.getCharacters(listOf(1, 2)) } returns Result.success(characters)

        // When
        val result = rickAndMortyApi.getCharacters(listOf(1, 2))

        // Then
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), characters)
        coVerify { rickAndMortyApi.getCharacters(listOf(1, 2)) }
    }

    @Test
    fun `get characters empty list`() = runTest {
        // Given
        val characters = emptyList<Character>()
        coEvery { rickAndMortyApi.getCharacters(emptyList()) } returns Result.success(characters)

        // When
        val result = rickAndMortyApi.getCharacters(emptyList())

        // Then
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), characters)
        coVerify { rickAndMortyApi.getCharacters(emptyList()) }
    }

    @Test
    fun `get characters failure`() = runTest {
        // Given
        val exception = Exception("Failed to fetch characters")
        coEvery { rickAndMortyApi.getCharacters(listOf(1, 2)) } returns Result.failure(exception)

        // When
        val result = rickAndMortyApi.getCharacters(listOf(1, 2))

        // Then
        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull(), exception)
        coVerify { rickAndMortyApi.getCharacters(listOf(1, 2)) }
    }

    @Test
    fun `get episode success`() = runTest {
        // Given
        val episode = episode
        coEvery { rickAndMortyApi.getEpisode(1) } returns Result.success(episode)

        // When
        val result = rickAndMortyApi.getEpisode(1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), episode)
        coVerify { rickAndMortyApi.getEpisode(1) }
    }

    @Test
    fun `get episode failure`() = runTest {
        // Given
        val exception = Exception("Failed to fetch episode")
        coEvery { rickAndMortyApi.getEpisode(1) } returns Result.failure(exception)

        // When
        val result = rickAndMortyApi.getEpisode(1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull(), exception)
        coVerify { rickAndMortyApi.getEpisode(1) }
    }

    @Test
    fun `get episodes success`() = runTest {
        // Given
        val episodes = episodes.take(2)
        coEvery { rickAndMortyApi.getEpisodes(listOf(1, 2)) } returns Result.success(episodes)

        // When
        val result = rickAndMortyApi.getEpisodes(listOf(1, 2))

        // Then
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), episodes)
        coVerify { rickAndMortyApi.getEpisodes(listOf(1, 2)) }
    }

    @Test
    fun `get location success`() = runTest {
        // Given
        val location = location
        coEvery { rickAndMortyApi.getLocation(1) } returns Result.success(location)

        // When
        val result = rickAndMortyApi.getLocation(1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), location)
        coVerify { rickAndMortyApi.getLocation(1) }
    }

    @Test
    fun `get location failure`() = runTest {
        // Given
        val exception = Exception("Failed to fetch location")
        coEvery { rickAndMortyApi.getLocation(1) } returns Result.failure(exception)

        // When
        val result = rickAndMortyApi.getLocation(1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull(), exception)
        coVerify { rickAndMortyApi.getLocation(1) }
    }

    @Test
    fun `get location page success`() = runTest {
        // Given
        val locationPage = locationPage
        coEvery { rickAndMortyApi.getLocationPage(1) } returns Result.success(locationPage)

        // When
        val result = rickAndMortyApi.getLocationPage(1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), locationPage)
        coVerify { rickAndMortyApi.getLocationPage(1) }
    }

    @Test
    fun `get location page failure`() = runTest {
        // Given
        val exception = Exception("Failed to fetch location page")
        coEvery { rickAndMortyApi.getLocationPage(1) } returns Result.failure(exception)

        // When
        val result = rickAndMortyApi.getLocationPage(1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull(), exception)
        coVerify { rickAndMortyApi.getLocationPage(1) }
    }

    @Test
    fun `get search location with parameter success`() = runTest {
        // Given
        val locationPage = locationPage
        coEvery { rickAndMortyApi.searchLocation(1, "Dimension C-137", "dimension") } returns Result.success(locationPage)

        // When
        val result = rickAndMortyApi.searchLocation(1, "Dimension C-137", "dimension")

        // Then
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), locationPage)
        coVerify { rickAndMortyApi.searchLocation(1, "Dimension C-137", "dimension") }
    }

    @Test
    fun `get search location with parameter failure`() = runTest {
        // Given
        val exception = Exception("Failed to search location")
        coEvery { rickAndMortyApi.searchLocation(1, "Dimension C-137", "dimension") } returns Result.failure(exception)

        // When
        val result = rickAndMortyApi.searchLocation(1, "Dimension C-137", "dimension")

        // Then
        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull(), exception)
        coVerify { rickAndMortyApi.searchLocation(1, "Dimension C-137", "dimension") }
    }

    @Test
    fun `get search location without parameter success`() = runTest {
        // Given
        val locationPage = locationPage
        coEvery { rickAndMortyApi.searchLocation(1, searchQuery = "Abadango", searchParameter = null) } returns Result.success(locationPage)

        // When
        val result = rickAndMortyApi.searchLocation(1, searchQuery = "Abadango", searchParameter = null)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), locationPage)
        coVerify { rickAndMortyApi.searchLocation(1, searchQuery = "Abadango", searchParameter = null) }
    }

    @Test
    fun `get search location without parameter failure`() = runTest {
        // Given
        val exception = Exception("Failed to search location")
        coEvery { rickAndMortyApi.searchLocation(1, searchQuery = "Abadango", searchParameter = null) } returns Result.failure(exception)

        // When
        val result = rickAndMortyApi.searchLocation(1, searchQuery = "Abadango", searchParameter = null)

        // Then
        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull(), exception)
        coVerify { rickAndMortyApi.searchLocation(1, searchQuery = "Abadango", searchParameter = null) }
    }

    companion object {
        private val characters = (0..10).map {
            Character(
                id = it,
                name = "Character $it",
                status = "",
                species = "",
                type = "",
                gender = CharacterGender.Male,
                origin = Location.default(),
                location = Location.default(),
                imageUrl = "",
                episodes = listOf(),
                url = "",
                created = "",
            )
        }
        private val episodes = (0..10).map {
            Episode(
                id = it,
                name = "Episode $it",
                seasonNumber = it,
                episodeNumber = it,
                charactersInEpisode = listOf(),
                airDate = "Date $it",
            )
        }
        private val locations = (0..10).map {
            Location(
                id = it,
                name = "Location $it",
                type = "",
                dimension = "",
                residents = listOf(),
                url = "",
                created = "",
                population = 0
            )
        }
        private val character = characters.first()
        private val location = locations.first()
        private val episode = episodes.first()
        private val characterPage = CharacterPage(
            info = CharacterPage.Info(
                count = 826,
                pages = 42,
                next = "https://rickandmortyapi.com/api/character?page=2",
                prev = null
            ),
            results = characters,
        )
        private val locationPage = LocationPage(
            info = LocationPage.LocationInfo(
                count = 126,
                pages = 7,
                next = "https://rickandmortyapi.com/api/location?page=2",
                prev = null
            ),
            results = locations,
        )
    }

}