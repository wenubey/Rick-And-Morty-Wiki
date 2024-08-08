package com.wenubey.data.remote


import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.wenubey.data.local.dao.CharacterDao
import com.wenubey.data.local.dao.RemoteKeyDao
import com.wenubey.data.local.dao.RemoteKeysEntity
import com.wenubey.domain.RickAndMortyApi
import com.wenubey.domain.model.Character
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
    private val searchQueryProvider: SearchQueryProvider,
    private val remoteKeyDao: RemoteKeyDao,
) : RemoteMediator<Int, Character>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>,
    ): MediatorResult = withContext(ioDispatcher) {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                remoteKeys?.prevKey ?: return@withContext MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey ?: return@withContext MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        val searchQuery = searchQueryProvider.getSearchQuery(DataTypeKey.CHARACTER)
        val result = if (searchQuery.isBlank()) {
            rickAndMortyApi.getCharacterPage(page)
        } else {
            rickAndMortyApi.searchCharacter(pageNumber = page, searchQuery = searchQuery)
        }

        if (result.isSuccess) {
            val characterPageDto = result.getOrNull()!!
            val characterEntities = characterPageDto.results

            if (loadType == LoadType.REFRESH) {
                dao.clearAll()
                remoteKeyDao.clearRemoteKeys()
            }

            val prevKey = if (page == 1) null else page.minus(1)
            val nextKey = characterPageDto.info.next?.let { page.plus(1) } // Use the `next` field to determine nextKey

            dao.insertAll(characterEntities)
            val remoteKeys = characterEntities.map {
                RemoteKeysEntity(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
            }
            remoteKeyDao.insertAll(remoteKeys)
            MediatorResult.Success(endOfPaginationReached = nextKey == null || characterEntities.isEmpty())
        } else {
            MediatorResult.Error(result.exceptionOrNull()!!)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Character>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                remoteKeyDao.remoteKeysRepoId(repoId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Character>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let {
                remoteKeyDao.remoteKeysRepoId(it.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Character>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let {
                remoteKeyDao.remoteKeysRepoId(it.id)
            }
    }
}