package com.wenubey.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.wenubey.data.RickAndMortyApi
import com.wenubey.data.getIdFromUrl
import com.wenubey.data.getIdFromUrls
import com.wenubey.data.local.CharacterEntity
import com.wenubey.data.local.toDomainCharacter
import com.wenubey.data.remote.dto.LocationDto
import com.wenubey.domain.model.Character
import com.wenubey.domain.repository.CharacterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val pager: Pager<Int, CharacterEntity>,
    private val ioDispatcher: CoroutineDispatcher,
) : CharacterRepository {

    override fun getCharacterPage(): Flow<PagingData<Character>> =
        pager.flow.map { pagingData ->
            pagingData.map { it.toDomainCharacter(null) }
        }


    override suspend fun getCharacter(id: Int): Result<Character> = withContext(ioDispatcher) {
        rickAndMortyApi.getCharacter(id).map { characterDto ->
            val locationEntity =
                (rickAndMortyApi.getLocation(characterDto.location.url.getIdFromUrl()).getOrNull()
                    ?: LocationDto.default())
                    .toLocationEntity()
            val originEntity =
                (rickAndMortyApi.getLocation(characterDto.origin.url.getIdFromUrl()).getOrNull()
                    ?: LocationDto.default()).toLocationEntity()
            val episodes = (rickAndMortyApi.getEpisodes(characterDto.episode.getIdFromUrls())
                .getOrNull())?.map { it.toDomainEpisode() } ?: listOf()
            characterDto.toCharacterEntity(locationEntity, originEntity).toDomainCharacter(episodes)
        }
    }

}