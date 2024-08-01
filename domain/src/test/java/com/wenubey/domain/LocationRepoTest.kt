package com.wenubey.domain

import androidx.paging.PagingData
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.LocationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationRepoTest {

    private val locationRepository: LocationRepository = mockk()

    @Test
    fun `get location - success`() = runTest {
        // Given
        val location = singleLocation
        coEvery { locationRepository.getLocation(1) } returns Result.success(location)

        // When
        val result = locationRepository.getLocation(1)

        // Then
        assert(result.isSuccess)
        assert(result.getOrNull() == location)
        coVerify { locationRepository.getLocation(1) }
    }

    @Test
    fun `get location - failure`() = runTest {
        // Given
        val exception = RuntimeException("Location not found")
        coEvery { locationRepository.getLocation(1) } returns Result.failure(exception)

        // When
        val result = locationRepository.getLocation(1)

        // Then
        assert(result.isFailure)
        assert(result.exceptionOrNull() == exception)
        coVerify { locationRepository.getLocation(1) }
    }

    @Test
    fun `get location page - success`() = runTest {
        // Given
        val locationPage = PagingData.from(listOf(singleLocation))
        coEvery { locationRepository.getLocationPage() } returns flowOf(locationPage)

        // When
        val flow = locationRepository.getLocationPage()
        val result = flow.first()

        // Then
        assertEquals(locationPage, result)
        coVerify { locationRepository.getLocationPage() }
    }

    @Test
    fun `get location page - empty`() = runTest {
        // Given
        val locationPage = PagingData.empty<Location>()
        coEvery { locationRepository.getLocationPage() } returns flowOf(locationPage)

        // When
        val flow = locationRepository.getLocationPage()
        val result = flow.first()

        // Then
        assertEquals(locationPage, result)
        coVerify { locationRepository.getLocationPage() }
    }


    companion object {
        private val singleLocation = Location(
            id = 1,
            name = "Earth",
            type = "Planet",
            dimension = "Dimension C-137",
            residents = listOf(),
            url = "https://rickandmortyapi.com/api/location/1",
            created = "",
            population = 0
        )
    }
}