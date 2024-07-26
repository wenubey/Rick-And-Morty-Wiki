package com.wenubey.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.wenubey.data.RickAndMortyApi
import com.wenubey.data.local.LocationEntity
import com.wenubey.data.local.toDomainCharacter
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val pager: Pager<Int, LocationEntity>,
    private val ioDispatcher: CoroutineDispatcher,
) : LocationRepository {

    override suspend fun getLocation(id: Int): Result<Location> = withContext(ioDispatcher) {
        rickAndMortyApi.getLocation(id).map { it.toLocationEntity().toDomainLocation() }
    }

    override fun getLocationPage(): Flow<PagingData<Location>> =
        pager.flow.map { pagingData -> pagingData.map { it.toDomainLocation() } }

    override suspend fun getCharactersById(ids: List<Int>): Result<List<Character>> = withContext(ioDispatcher) {
        rickAndMortyApi.getCharacters(ids)
            .map { characterDtoList ->
                characterDtoList.map { characterDto ->
                    characterDto
                        .toCharacterEntity(null, null)
                        .toDomainCharacter(null)
                }
            }
    }
}