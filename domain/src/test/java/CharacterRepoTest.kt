import androidx.paging.PagingData
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.CharacterGender
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.CharacterRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterRepoTest {

    private val characterRepository: CharacterRepository = mockk()

    @Test
    fun `get character by id - success`() = runTest {
        // Given
        val character = singleCharacter
        coEvery { characterRepository.getCharacter(1) } returns Result.success(character)

        // When
        val result = characterRepository.getCharacter(1)

        // Then
        assert(result.isSuccess)
        assertEquals(character, result.getOrNull())
        coVerify { characterRepository.getCharacter(1) }

    }

    @Test
    fun `get character by id - failure`() = runTest {
        // Given
        val exception = RuntimeException("Character not found")
        coEvery { characterRepository.getCharacter(1) } returns Result.failure(exception)

        // When
        val result = characterRepository.getCharacter(1)

        // Then
        assert(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify { characterRepository.getCharacter(1) }
    }

    @Test
    fun `get character page - success`() = runTest {
        // Given
        val characterPage = PagingData.from(listOf(singleCharacter))
        coEvery { characterRepository.getCharacterPage() } returns flowOf(characterPage)

        // When
        val flow = characterRepository.getCharacterPage()
        val result = flow.first()

        // Then
        assertEquals(characterPage, result)
        coVerify { characterRepository.getCharacterPage() }
    }

    @Test
    fun `get character page - empty`() = runTest {
        // Given
        val characterPage = PagingData.empty<Character>()
        coEvery { characterRepository.getCharacterPage() } returns flowOf(characterPage)

        // When
        val flow = characterRepository.getCharacterPage()
        val result = flow.first()

        // Then
        assertEquals(characterPage, result)
        coVerify { characterRepository.getCharacterPage() }
    }

    companion object {
        private val singleCharacter = Character(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = CharacterGender.Male,
            origin = Location.default(),
            location = Location.default(),
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            episodes = listOf(),
            url = "https://rickandmortyapi.com/api/character/1",
            created = ""
        )
    }
}