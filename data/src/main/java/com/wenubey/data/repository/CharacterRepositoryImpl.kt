package com.wenubey.data.repository


import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.wenubey.data.KtorClient
import com.wenubey.data.getIdFromUrl
import com.wenubey.data.getIdFromUrls
import com.wenubey.data.local.CharacterEntity
import com.wenubey.data.local.toDomainCharacter
import com.wenubey.data.remote.dto.LocationDto
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Episode
import com.wenubey.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val ktorClient: KtorClient,
    private val pager: Pager<Int, CharacterEntity>,
) : CharacterRepository {

    override fun getCharacterPage(): Flow<PagingData<Character>> =
        pager.flow.map { pagingData ->
            pagingData.map { it.toDomainCharacter(null) }
        }


    override suspend fun getCharacter(id: Int): Result<Character> {
        return ktorClient.getCharacter(id).map { characterDto ->
            val locationEntity =
                (ktorClient.getLocation(characterDto.location.url.getIdFromUrl()).getOrNull()
                    ?: LocationDto.default())
                    .toLocationEntity()
            val originEntity =
                (ktorClient.getLocation(characterDto.origin.url.getIdFromUrl()).getOrNull()
                    ?: LocationDto.default()).toLocationEntity()
            val episodes = (ktorClient.getEpisodes(characterDto.episode.getIdFromUrls()).getOrNull())?.map { it.toDomainEpisode() } ?: listOf()
            characterDto.toCharacterEntity(locationEntity, originEntity).toDomainCharacter(episodes)
        }
    }

}