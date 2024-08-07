package com.wenubey.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.wenubey.data.local.dao.LocationDao
import com.wenubey.domain.RickAndMortyApi
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.SearchQueryProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class LocationsRemoteMediator @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val dao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val searchQueryProvider: SearchQueryProvider
) : RemoteMediator<Int, Location>() {
    private var nextPageNumber: Int = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Location>
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
                val locationEntities = locationPageDto.results
                if (loadType == LoadType.REFRESH) {
                    dao.clearAll()
                }
                dao.insertAll(locationEntities)

                val endOfPaginationReached = locationPageDto.info.next == null
                Timber.d("LocationsRemoteMediator:Success")
                return@withContext MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }
            .onFailure { e ->
                Timber.e(e, "LocationsRemoteMediator:Error")
                return@withContext MediatorResult.Error(e)
            }
        MediatorResult.Success(endOfPaginationReached = true)
    }
}