import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SettingsRepoTest {

    private lateinit var settingsRepository: SettingsRepository

    @Before
    fun setup() {
        settingsRepository = mockk()
    }

    @Test
    fun `isLinearLayout value fetch - success`() = runTest {
        // Given
        val isLinearLayout = true
        coEvery { settingsRepository.isLinearLayout } returns flowOf(isLinearLayout)

        // When
        val result = settingsRepository.isLinearLayout

        // Then
        assert(result.first() == isLinearLayout)
        coVerify { settingsRepository.isLinearLayout }
    }

    @Test
    fun `isNightMode value fetch - success`() = runTest {
        // Given
        val isNightMode = true
        coEvery { settingsRepository.isNightMode } returns flowOf(isNightMode)

        // When
        val result = settingsRepository.isNightMode

        // Then
        assert(result.first() == isNightMode)
        coVerify { settingsRepository.isNightMode }
    }

    @Test
    fun `isScreenLocked value fetch - success`() = runTest {
        // Given
        val isScreenLocked = true
        coEvery { settingsRepository.isScreenLocked } returns flowOf(isScreenLocked)

        // When
        val result = settingsRepository.isScreenLocked

        // Then
        assert(result.first() == isScreenLocked)
        coVerify { settingsRepository.isScreenLocked }
    }

    @Test
    fun `isTopBarLocked value fetch - success`() = runTest {
        // Given
        val isTopBarLocked = true
        coEvery { settingsRepository.isTopBarLocked } returns flowOf(isTopBarLocked)

        // When
        val result = settingsRepository.isTopBarLocked

        // Then
        assert(result.first() == isTopBarLocked)
        coVerify { settingsRepository.isTopBarLocked }
    }

    @Test
    fun `get search history - success`() = runTest {
        // Given
        val dataTypeKey = DataTypeKey.CHARACTER
        val mockSearchHistory = listOf("Rick Sanchez", "Morty Smith")
        coEvery { settingsRepository.getSearchHistory(dataTypeKey) } returns flowOf(mockSearchHistory)

        // When
        val result = settingsRepository.getSearchHistory(dataTypeKey)

        // Then
        assert(result.first() == mockSearchHistory)
        coVerify { settingsRepository.getSearchHistory(dataTypeKey) }
    }

    @Test
    fun `get search history - empty`() = runTest {
        // Given
        val dataTypeKey = DataTypeKey.CHARACTER
        val mockSearchHistory = listOf<String>()
        coEvery { settingsRepository.getSearchHistory(dataTypeKey) } returns flowOf(mockSearchHistory)

        // When
        val result = settingsRepository.getSearchHistory(dataTypeKey)

        // Then
        assert(result.first() == mockSearchHistory)
        coVerify { settingsRepository.getSearchHistory(dataTypeKey) }
    }

    @Test
    fun `save layout preference - success`() = runTest {
        // Given
        val isLinearLayout = true
        coEvery { settingsRepository.saveLayoutPreference(isLinearLayout) } returns Unit

        // When
        settingsRepository.saveLayoutPreference(isLinearLayout)

        // Then
        coVerify { settingsRepository.saveLayoutPreference(isLinearLayout) }
    }

    @Test
    fun `save night mode preference - success`() = runTest {
        // Given
        val isNightMode = true
        coEvery { settingsRepository.saveNightModePreference(isNightMode) } returns Unit

        // When
        settingsRepository.saveNightModePreference(isNightMode)

        // Then
        coVerify { settingsRepository.saveNightModePreference(isNightMode) }
    }

    @Test
    fun `save screen locked preference - success`() = runTest {
        // Given
        val isScreenLocked = true
        coEvery { settingsRepository.saveScreenLockedPreference(isScreenLocked) } returns Unit

        // When
        settingsRepository.saveScreenLockedPreference(isScreenLocked)

        // Then
        coVerify { settingsRepository.saveScreenLockedPreference(isScreenLocked) }
    }

    @Test
    fun `save top bar locked preference - success`() = runTest {
        // Given
        val isTopBarLocked = true
        coEvery { settingsRepository.saveTopBarLockedPreference(isTopBarLocked) } returns Unit

        // When
        settingsRepository.saveTopBarLockedPreference(isTopBarLocked)

        // Then
        coVerify { settingsRepository.saveTopBarLockedPreference(isTopBarLocked) }
    }

    @Test
    fun `save search history - success`() = runTest {
        // Given
        val dataTypeKey = DataTypeKey.CHARACTER
        val searchQuery = "Rick Sanchez"
        coEvery { settingsRepository.saveSearchHistory(dataTypeKey, searchQuery) } returns Unit

        // When
        settingsRepository.saveSearchHistory(dataTypeKey, searchQuery)

        // Then
        coVerify { settingsRepository.saveSearchHistory(dataTypeKey, searchQuery) }
    }

    @Test
    fun `clean all search history - success`() = runTest {
        // Given
        coEvery { settingsRepository.cleanAllSearchHistory() } just runs

        // When
        settingsRepository.cleanAllSearchHistory()

        // Then
        coVerify { settingsRepository.cleanAllSearchHistory() }
    }

}
