package com.wenubey.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.wenubey.domain.RickAndMortyApi
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val pager: Pager<Int, Location>,
    private val ioDispatcher: CoroutineDispatcher,
) : LocationRepository {

    override suspend fun getLocation(id: Int): Result<Location> = withContext(ioDispatcher) {
        rickAndMortyApi.getLocation(id)
    }

    override fun getLocationPage(): Flow<PagingData<Location>> =
        pager.flow

}