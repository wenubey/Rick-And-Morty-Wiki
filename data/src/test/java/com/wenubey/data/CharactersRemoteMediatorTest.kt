package com.wenubey.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.wenubey.data.local.dao.CharacterDao
import com.wenubey.data.local.dao.RemoteKeyDao
import com.wenubey.data.local.dao.RemoteKeysEntity
import com.wenubey.data.remote.CharactersRemoteMediator
import com.wenubey.domain.RickAndMortyApi
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.CharacterPage
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.SearchQueryProvider
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
class CharactersRemoteMediatorTest {

    private val api: RickAndMortyApi = mockk()
    private val dao: CharacterDao = mockk {
        coEvery { insertAll(any()) } just Runs
        coEvery { clearAll() } just Runs
    }
    private val remoteKeyDao: RemoteKeyDao = mockk {
        coEvery { insertAll(any()) } just Runs
        coEvery { clearRemoteKeys() } just Runs
        coEvery { remoteKeysRepoId(any()) } coAnswers  { RemoteKeysEntity(repoId = 11, prevKey = null, nextKey = 2) }
    }
    private val searchQueryProvider: SearchQueryProvider = mockk()
    private val pagingState = mockk<PagingState<Int, Character>> {
        every { config } returns PagingConfig(10)
    }

    private lateinit var testDispatcher: TestDispatcher


    private val characterList = (1..40).map {
        Character.default().copy(id = it)
    }

    private val characterPage = CharacterPage(
        info = CharacterPage.Info(
            count = 1,
            pages = 1,
            next = "page=2",
            prev = null,
        ),
        results = characterList
    )

    @Before
    fun setup() {
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun `Characters Remote Mediator Load Success - Refresh`() = runTest {
        // Given
        every { searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER) } returns ""
        coEvery { api.getCharacterPage(1) } coAnswers { Result.success(characterPage) }

        val mockPagingState = mockk<PagingState<Int, Character>>(relaxed = true)
        every { mockPagingState.anchorPosition } returns 0
        every { mockPagingState.closestItemToPosition(any()) } returns characterPage.results.first()

        // When
        val result = createRemoteMediator().load(LoadType.REFRESH, mockPagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify { api.getCharacterPage(1) }
    }

    @Test
    fun `Characters Remote Mediator Load Success - Append`() = runTest {
        // Given
        val lastCharacter = Character.default().copy(id = 11)
        every { searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER) } returns ""
        coEvery { pagingState.lastItemOrNull() } coAnswers { lastCharacter }

        val page = PagingSource.LoadResult.Page(
            data = characterList,
            prevKey = null,
            nextKey = 2
        )

        every { pagingState.pages } returns listOf(page)
        coEvery { api.getCharacterPage(any()) } coAnswers { Result.success(characterPage) }

        // When
        val remoteMediator = createRemoteMediator()
        val result = remoteMediator.load(LoadType.APPEND, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify { api.getCharacterPage(2) }
    }

    @Test
    fun `Characters Remote Mediator Load Success - Prepend`() = runTest {
        // Given
        val firstCharacter = Character.default().copy(id = 1)
        every { searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER) } returns ""
        coEvery { pagingState.firstItemOrNull() } coAnswers { firstCharacter }

        val page = PagingSource.LoadResult.Page(
            data = characterList,
            prevKey = 1,
            nextKey = null
        )
        every { pagingState.pages } returns listOf(page)

        coEvery { api.getCharacterPage(any()) } coAnswers { Result.success(characterPage) }

        // When
        val remoteMediator = createRemoteMediator()
        val result = remoteMediator.load(LoadType.PREPEND, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `Characters Remote Mediator Load Failure`() = runTest {
        // Given
        every { searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER) } returns ""
        coEvery { api.getCharacterPage(1) } coAnswers { Result.failure(Exception("Network error")) }
        every { pagingState.anchorPosition } returns null

        // When
        val result = createRemoteMediator().load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertTrue((result as RemoteMediator.MediatorResult.Error).throwable is Exception)
    }

    @Test
    fun `Characters Remote Mediator Load Empty Data`() = runTest {
        // Given
        every { searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER) } returns ""
        val emptyPage = characterPage.copy(results = emptyList())
        coEvery { api.getCharacterPage(1) } coAnswers { Result.success(emptyPage) }
        every { pagingState.anchorPosition } returns null

        // When
        val result = createRemoteMediator().load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `Characters Remote Mediator Load End of Pagination`() = runTest {
        // Given
        val lastCharacter = Character.default().copy(id = 10)
        every { searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER) } returns ""
        val lastPage = characterPage.copy(
            info = characterPage.info.copy(next = null) // Mark as the last page
        )

        coEvery { api.getCharacterPage(1) } coAnswers { Result.success(lastPage) }
        every { pagingState.anchorPosition } returns 10
        every { pagingState.closestItemToPosition(10) } returns lastCharacter

        // When
        val result = createRemoteMediator().load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify { api.getCharacterPage(1) }
    }


    @Test
    fun `Characters Remote Mediator Load Success - Append End of Pagination`() = runTest {
        // Given
        every { searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER) } returns ""
        val lastCharacter = characterList.last()
        val lastPage = characterPage.copy(info = characterPage.info.copy(next = null))
        coEvery { api.getCharacterPage(2) } coAnswers { Result.success(lastPage) }

        val mockPagingState = mockk<PagingState<Int, Character>>(relaxed = true)
        every { mockPagingState.anchorPosition } returns null
        every { mockPagingState.lastItemOrNull() } returns lastCharacter


        val page = PagingSource.LoadResult.Page(
            data = characterList,
            prevKey = 1,
            nextKey = null
        )
        every { mockPagingState.pages } returns listOf(page)

        // When
        val result = createRemoteMediator().load(LoadType.APPEND, mockPagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify { api.getCharacterPage(2) }
    }


    @Test
    fun `Characters Remote Mediator Handles Inconsistent Data`() = runTest {
        // Given
        every { searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER) } returns ""
        val inconsistentPage = characterPage.copy(results = emptyList())
        coEvery { api.getCharacterPage(1) } coAnswers { Result.success(inconsistentPage) }
        every { pagingState.anchorPosition } returns 0
        every { pagingState.closestItemToPosition(0) } returns null

        // When
        val result = createRemoteMediator().load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify { api.getCharacterPage(1) }
    }

    @Test
    fun `Characters Remote Mediator Handle Prepend with No Previous Page`() = runTest {
        // Given
        every { searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER) } returns ""
        coEvery { pagingState.firstItemOrNull() } coAnswers { characterList.first() }
        coEvery { api.getCharacterPage(any()) } coAnswers { Result.success(characterPage) }

        val page = PagingSource.LoadResult.Page(
            data = characterList,
            prevKey = null,
            nextKey = 2
        )
        every { pagingState.pages } returns listOf(page)

        // When
        val remoteMediator = createRemoteMediator()
        val result = remoteMediator.load(LoadType.PREPEND, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify(exactly = 0) { api.getCharacterPage(any()) }
    }

    @Test
    fun `Characters Remote Mediator Load Success - Refresh with Search Query`() = runTest {
        // Given
        every { searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER) } returns "Rick"
        coEvery { api.searchCharacter(1, "Rick") } coAnswers { Result.success(characterPage) }

        // Mock PagingState
        val mockPagingState = mockk<PagingState<Int, Character>>(relaxed = true)
        every { mockPagingState.anchorPosition } returns 0
        every { mockPagingState.closestItemToPosition(any()) } returns characterPage.results.first()

        // When
        val result = createRemoteMediator().load(LoadType.REFRESH, mockPagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify { api.searchCharacter(1, "Rick") }
    }


    private fun createRemoteMediator() =
        CharactersRemoteMediator(
            rickAndMortyApi = api,
            dao = dao,
            ioDispatcher = testDispatcher,
            searchQueryProvider = searchQueryProvider,
            remoteKeyDao = remoteKeyDao
        )


}