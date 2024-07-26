package com.wenubey.data.remote


import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.wenubey.data.RickAndMortyApi
import com.wenubey.data.local.CharacterEntity
import com.wenubey.data.local.dao.CharacterDao
import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.SearchQueryProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val dao: CharacterDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val searchQueryProvider: SearchQueryProvider
) : RemoteMediator<Int, CharacterEntity>() {
    private var nextPageNumber: Int = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>,
    ): MediatorResult = withContext(ioDispatcher) {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    nextPageNumber = 1
                    1
                }

                LoadType.PREPEND -> return@withContext MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastCharacter = state.lastItemOrNull()
                    if (lastCharacter == null) {
                        1
                    } else {
                        nextPageNumber++
                    }
                }
            }
            val searchQuery = searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER)

            if (searchQuery.isBlank()) {
                rickAndMortyApi.getCharacterPage(page)
            } else {
                rickAndMortyApi.searchCharacter(pageNumber = page, searchQuery = searchQuery)
            }
                .onSuccess { characterPageDto ->
                    val characterEntities = characterPageDto.results.map { it.toCharacterEntity(null, null) }

                    if (loadType == LoadType.REFRESH) {
                        dao.clearAll()
                    }
                    dao.insertAll(characterEntities)
                    val endOfPaginationReached = characterPageDto.info.next == null
                    return@withContext MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }
                .onFailure { e ->
                    return@withContext MediatorResult.Error(e)
                }
            MediatorResult.Success(endOfPaginationReached = true)
    }


}