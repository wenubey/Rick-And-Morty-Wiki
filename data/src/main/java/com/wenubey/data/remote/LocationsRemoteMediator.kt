package com.wenubey.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.wenubey.data.RickAndMortyApi
import com.wenubey.data.local.LocationEntity
import com.wenubey.data.local.dao.LocationDao
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.SearchQueryProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class LocationsRemoteMediator @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val dao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val searchQueryProvider: SearchQueryProvider
) : RemoteMediator<Int, LocationEntity>() {
    private var nextPageNumber: Int = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocationEntity>
    ): MediatorResult = withContext(ioDispatcher) {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                nextPageNumber = 1
                1
            }

            LoadType.PREPEND -> return@withContext MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastLocation = state.lastItemOrNull()
                if (lastLocation == null) {
                    1
                } else {
                    nextPageNumber++
                }
            }
        }

        val searchQuery = searchQueryProvider.getSearchQuery(DataTypeKey.LOCATION)
        val queryParameters = searchQuery.split(",")
        if (searchQuery.isBlank()) {
            rickAndMortyApi.getLocationPage(page)
        } else {
            if (queryParameters.size > 1) {
                rickAndMortyApi.searchLocation(
                    pageNumber = page,
                    searchQuery = queryParameters[1],
                    searchParameter = queryParameters[0]
                )
            } else {
                rickAndMortyApi.searchLocation(page, searchQuery)
            }
        }
            .onSuccess { locationPageDto ->
                val locationEntities = locationPageDto.results.map { it.toLocationEntity() }
                if (loadType == LoadType.REFRESH) {
                    dao.clearAll()
                }
                dao.insertAll(locationEntities)

                val endOfPaginationReached = locationPageDto.info.next == null
                return@withContext MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }
            .onFailure {
                return@withContext MediatorResult.Error(it)
            }
        MediatorResult.Success(endOfPaginationReached = true)
    }
}