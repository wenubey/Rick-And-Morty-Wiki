package com.wenubey.data.repository

import androidx.paging.PagingData
import com.wenubey.data.KtorClient
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(
    private val ktorClient: KtorClient,
) : LocationRepository {

    override suspend fun getLocation(id: Int): Result<Location> {
        return ktorClient.getLocation(id).map { it.toLocationEntity().toDomainLocation() }
    }

    override suspend fun getLocationPage(name: String?): Flow<PagingData<Location>> {
        TODO("Not yet implemented PAGER IMPLEMENTATION REQUIRED.")
    }
}