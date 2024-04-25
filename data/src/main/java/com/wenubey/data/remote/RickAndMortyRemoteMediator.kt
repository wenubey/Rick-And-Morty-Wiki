package com.wenubey.data.remote


import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.wenubey.data.KtorClient
import com.wenubey.data.local.CharacterEntity
import com.wenubey.data.local.RickAndMortyDao
import com.wenubey.data.remote.dto.toCharacterEntity
import com.wenubey.domain.repository.SearchQueryProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class RickAndMortyRemoteMediator @Inject constructor(
    private val ktorClient: KtorClient,
    private val dao: RickAndMortyDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val searchQueryProvider: SearchQueryProvider
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>,
    ): MediatorResult = withContext(ioDispatcher) {
        return@withContext try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return@withContext MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastCharacter = state.lastItemOrNull()
                    if (lastCharacter == null) {
                        return@withContext MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                       (lastCharacter.id / state.config.pageSize) + 1
                    }
                }
            }
            val searchQuery = searchQueryProvider.getSearchQuery()

            val characters = if (searchQuery.isBlank() || searchQuery.isEmpty()) {
                ktorClient.getCharacterPage(page)
            } else {
                ktorClient.searchCharacter(searchQuery)
            }

            val characterEntities = characters.results.map { it.toCharacterEntity() }
            if (loadType == LoadType.REFRESH) {
                dao.clearAll()
            }
            dao.insertAll(characterEntities)

            val endOfPaginationReached = characters.info.next == null
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}