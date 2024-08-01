package com.wenubey.domain

import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.SearchQueryProvider
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchQueryProviderTest {

    private lateinit var searchQueryProvider: SearchQueryProvider

    @Before
    fun setup() {
        searchQueryProvider = mockk()
    }

    @Test
    fun `get and set search query for a key`() {
        // Given
        val key = DataTypeKey.CHARACTER
        val initialQuery = "Rick Sanchez"
        val newQuery = "Morty Smith"

        every { searchQueryProvider.getSearchQuery(key) } returnsMany listOf(initialQuery, newQuery)
        every { searchQueryProvider.setSearchQuery(key, newQuery) } just runs

        // When
        val initialResult = searchQueryProvider.getSearchQuery(key)
        searchQueryProvider.setSearchQuery(key, newQuery)
        val updatedResult = searchQueryProvider.getSearchQuery(key)

        // Then
        assertEquals(initialQuery, initialResult)
        verify { searchQueryProvider.setSearchQuery(key, newQuery) }
        verify { searchQueryProvider.getSearchQuery(key) }
        assertEquals(newQuery, updatedResult)
    }

    @Test
    fun `get  search queries for multiple keys`() = runTest {
        // Given
        val key1 = DataTypeKey.CHARACTER
        val key2 = DataTypeKey.LOCATION
        val initialQuery1 = "Rick Sanchez"
        val initialQuery2 = "Earth"

        every { searchQueryProvider.setSearchQuery(any(), any()) } just runs
        every { searchQueryProvider.getSearchQuery(key1) } returns initialQuery1
        every { searchQueryProvider.getSearchQuery(key2) } returns initialQuery2

        // When
        val result1 = searchQueryProvider.getSearchQuery(key1)
        val result2 = searchQueryProvider.getSearchQuery(key2)

        // Then
        assertEquals(initialQuery1, result1)
        assertEquals(initialQuery2, result2)
    }
}