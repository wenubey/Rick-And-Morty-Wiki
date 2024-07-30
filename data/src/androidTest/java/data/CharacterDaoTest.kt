package data

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wenubey.data.local.RickAndMortyDatabase
import com.wenubey.data.local.dao.CharacterDao
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.CharacterGender
import com.wenubey.domain.model.Location
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterDaoTest {

    private lateinit var database: RickAndMortyDatabase
    private lateinit var characterDao: CharacterDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(context, RickAndMortyDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        characterDao = database.characterDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAllAndPagingSource() = runBlocking {
        // When
        characterDao.insertAll(characters)
        val allCharacters = characterDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = Int.MAX_VALUE,
                placeholdersEnabled = false
            )
        )

        // Then
        assertEquals(characters, (allCharacters as PagingSource.LoadResult.Page).data)
    }

    @Test
    fun testClearAll() = runBlocking {
        // When
        characterDao.insertAll(characters)
        characterDao.clearAll()
        val allCharacters = characterDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = Int.MAX_VALUE,
                placeholdersEnabled = false
            )
        )

        // Then
        assertEquals(0, (allCharacters as PagingSource.LoadResult.Page).data.size)
    }

    @Test
    fun testPagingSource_loading_InitialPage() = runBlocking {
        // When
        characterDao.insertAll(characters)

        val pagingSource = characterDao.pagingSource()

        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(characters.take(20), page.data)
    }

    @Test
    fun testPagingSource_largeDataset() = runBlocking {
        // When
        characterDao.insertAll(characters)

        val loadResult = characterDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = characters.size,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(characters, page.data)
    }

    @Test
    fun testPagingSource_secondPage() = runBlocking {
        // When
        characterDao.insertAll(characters)

        val secondLoadResult = characterDao.pagingSource().load(
            PagingSource.LoadParams.Append(
                key = 20,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(secondLoadResult is PagingSource.LoadResult.Page)
        val page = secondLoadResult as PagingSource.LoadResult.Page
        assertEquals(characters.drop(20).take(20), page.data)
    }

    @Test
    fun testPagingSource_emptyDataset() = runBlocking {
        // When
        val loadResult = characterDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = Int.MAX_VALUE,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(emptyList<Character>(), page.data)
    }

    @Test
    fun testPagingSource_lastPage() = runBlocking {
        // When
        characterDao.insertAll(characters)

        val pagingSource = characterDao.pagingSource()

        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Append(
                key = characters.size - 20,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(characters.takeLast(20), page.data)
    }

    companion object {
        private const val CHARACTER_LOCATIONS = 100
        private val characters = (1..CHARACTER_LOCATIONS).map {
            Character(
                id = it,
                name = "Character $it",
                status = "Alive",
                species = "Human",
                type = "",
                gender = CharacterGender.Male,
                origin = Location.default(),
                location = Location.default(),
                imageUrl = "https://rickandmortyapi.com/api/character/avatar/$it.jpeg",
                episodes = listOf(),
                url = "https://rickandmortyapi.com/api/character/$it",
                created = ""
            )
        }
    }

}