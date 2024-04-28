package com.wenubey.data.remote


import android.util.Log
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
    private var nextPageNumber: Int = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>,
    ): MediatorResult = withContext(ioDispatcher) {
        return@withContext try {
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

            val searchQuery = searchQueryProvider.getSearchQuery()

            val characters = if (searchQuery.isBlank()) {
                ktorClient.getCharacterPage(page)
            } else {
                ktorClient.searchCharacter(pageNumber = page, searchQuery = searchQuery)
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