package com.wenubey.data.remote


import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.wenubey.data.KtorClient
import com.wenubey.data.getIdFromUrl
import com.wenubey.data.local.CharacterEntity
import com.wenubey.data.local.dao.CharacterDao
import com.wenubey.data.remote.dto.CharacterPageDto
import com.wenubey.data.remote.dto.LocationDto
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.SearchQueryProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator @Inject constructor(
    private val ktorClient: KtorClient,
    private val dao: CharacterDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val searchQueryProvider: SearchQueryProvider
) : RemoteMediator<Int, CharacterEntity>() {
    private var nextPageNumber: Int = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>,
    ): MediatorResult = withContext(ioDispatcher) {
        return@withContext try {
            Log.i(TAG, "loadType: $loadType")
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    nextPageNumber = 1
                    1
                }
                LoadType.PREPEND -> return@withContext MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastCharacter = state.lastItemOrNull()
                    Log.i(TAG, "lastCharacter.name:${lastCharacter?.name}")
                    if (lastCharacter == null) {
                        1
                    } else {
                        nextPageNumber++
                    }
                }
            }
            Log.i(TAG, "Page: $page")
            val searchQuery = searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER)

            if (searchQuery.isBlank()) {
                ktorClient.getCharacterPage(page)
            } else {
                ktorClient.searchCharacter(pageNumber = page, searchQuery = searchQuery)
            }
                .onSuccess { characterPageDto ->
                    val characterEntities =
                        getCharacterEntities(characters = characterPageDto, ktorClient = ktorClient)
                    if (loadType == LoadType.REFRESH) {
                        dao.clearAll()
                    }
                    dao.insertAll(characterEntities)
                    val endOfPaginationReached = characterPageDto.info.next == null
                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }
                .onFailure { e ->
                    MediatorResult.Error(e)
                }

            MediatorResult.Success(endOfPaginationReached = true)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getCharacterEntities(
        characters: CharacterPageDto,
        ktorClient: KtorClient
    ): List<CharacterEntity> {
        return characters.results.map { characterDto ->
            val locationId = characterDto.location.url.getIdFromUrl()
            val locationDto = if (locationId == -1) {
                LocationDto.default()
            } else {
                ktorClient.getLocation(locationId).getOrNull()!!
            }
            val originId = characterDto.origin.url.getIdFromUrl()
            val originDto = if (originId == -1) {
                LocationDto.default()
            } else {
                ktorClient.getLocation(originId).getOrNull()!!
            }
            val locationEntity = locationDto.toLocationEntity()
            val originEntity = originDto.toLocationEntity()
            characterDto.toCharacterEntity(locationEntity, originEntity)
        }
    }

    private companion object {
        const val TAG= "charactersRemoteMediator"
    }
}