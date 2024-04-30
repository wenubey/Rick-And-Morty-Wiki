package com.wenubey.data.repository


import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.wenubey.data.KtorClient
import com.wenubey.data.getIdFromUrl
import com.wenubey.data.local.CharacterEntity
import com.wenubey.data.local.toDomainCharacter
import com.wenubey.data.remote.dto.LocationDto
import com.wenubey.data.remote.dto.OriginDto
import com.wenubey.domain.model.Character
import com.wenubey.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val ktorClient: KtorClient,
    private val pager: Pager<Int, CharacterEntity>,
) : CharacterRepository {

    override fun getCharacterPage(name: String?): Flow<PagingData<Character>> =
        pager.flow.map { pagingData ->
            pagingData.map { it.toDomainCharacter() }
        }


    override suspend fun getCharacter(id: Int): Result<Character> {
        return ktorClient.getCharacter(id).map { characterDto ->
            val locationDto =
                ktorClient.getLocation(characterDto.location.url.getIdFromUrl()).getOrNull()
                    ?: LocationDto.default()
            val originDto = ktorClient.getOrigin(characterDto.origin.url.getIdFromUrl()).getOrNull()
                ?: OriginDto.default()
            characterDto
                .toCharacterEntity(locationDto, originDto)
                .toDomainCharacter()
        }
    }

    override suspend fun getLocationResidents(residentUrls: List<Int>): Result<List<Character>> {
        return ktorClient.getCharacters(residentUrls).map { characters ->
            characters.map { characterDto ->
                val locationDto =
                    ktorClient.getLocation(characterDto.location.url.getIdFromUrl()).getOrNull()
                        ?: LocationDto.default()
                val originDto =
                    ktorClient.getOrigin(characterDto.origin.url.getIdFromUrl()).getOrNull()
                        ?: OriginDto.default()
                characterDto
                    .toCharacterEntity(locationDto, originDto)
                    .toDomainCharacter()
            }
        }
    }
}