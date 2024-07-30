package data

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.wenubey.data.local.RickAndMortyDatabase
import com.wenubey.data.local.dao.LocationDao
import com.wenubey.domain.model.Location
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LocationDaoTest {

    private lateinit var database: RickAndMortyDatabase
    private lateinit var locationDao: LocationDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(context, RickAndMortyDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        locationDao = database.locationDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAllAndPagingSource() = runBlocking {
        // When
        locationDao.insertAll(locations)
        val allLocations = locationDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = Int.MAX_VALUE,
                placeholdersEnabled = false
            )
        )

        // Then
        assertEquals(locations, (allLocations as PagingSource.LoadResult.Page).data)
    }

    @Test
    fun testClearAll() = runBlocking {
        // When
        locationDao.insertAll(locations)
        locationDao.clearAll()
        val allLocations = locationDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = Int.MAX_VALUE,
                placeholdersEnabled = false
            )
        )

        // Then
        assertEquals(0, (allLocations as PagingSource.LoadResult.Page).data.size)
    }

    @Test
    fun testPagingSource_loading_InitialPage() = runBlocking {
        // When
        locationDao.insertAll(locations)

        val pagingSource = locationDao.pagingSource()

        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(locations.take(20), page.data)
    }

    @Test
    fun testPagingSource_largeDataset() = runBlocking {
        // When
        locationDao.insertAll(locations)

        val loadResult = locationDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = locations.size,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(locations, page.data)
    }

    @Test
    fun testPagingSource_secondPage() = runBlocking {

        // When
        locationDao.insertAll(locations)

        val secondLoadResult = locationDao.pagingSource().load(
            PagingSource.LoadParams.Append(
                key = 20,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(secondLoadResult is PagingSource.LoadResult.Page)
        val page = secondLoadResult as PagingSource.LoadResult.Page
        assertEquals(locations.drop(20).take(20), page.data)
    }

    @Test
    fun testPagingSource_emptyDataset() = runBlocking {
        // When
        val loadResult = locationDao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = Int.MAX_VALUE,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(emptyList<Location>(), page.data)
    }

    @Test
    fun testPagingSource_lastPage() = runBlocking {
        // When
        locationDao.insertAll(locations)

        val pagingSource = locationDao.pagingSource()

        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Append(
                key = locations.size - 20,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(locations.takeLast(20), page.data)
    }


    companion object {
        private const val NUM_LOCATIONS = 100
        private val locations = (1..NUM_LOCATIONS).map {
            Location(
                id = it,
                name = "Location $it",
                dimension = "Dimension $it",
                residents = listOf(),
                type = "Type $it",
                population = it,
                url = "https://rickandmortyapi.com/api/location/$it",
                created = ""
            )
        }
    }
}