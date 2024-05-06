package com.wenubey.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.wenubey.data.KtorClient
import com.wenubey.data.local.LocationEntity
import com.wenubey.data.local.toDomainCharacter
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val ktorClient: KtorClient,
    private val pager: Pager<Int, LocationEntity>
) : LocationRepository {

    override suspend fun getLocation(id: Int): Result<Location> {
        return ktorClient.getLocation(id).map { it.toLocationEntity().toDomainLocation() }
    }

    override suspend fun getLocationPage(): Flow<PagingData<Location>> =
        pager.flow.map { pagingData ->
            pagingData.map { it.toDomainLocation() }
        }

    override suspend fun getCharactersById(ids: List<Int>): Result<List<Character>> {
        return ktorClient.getCharacters(ids)
            .map { characterDtoList ->
                characterDtoList.map { characterDto ->
                    characterDto
                        .toCharacterEntity(null, null)
                        .toDomainCharacter(null)
                }
            }
    }
}