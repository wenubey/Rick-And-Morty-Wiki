package com.wenubey.data

import com.wenubey.data.remote.dto.CharacterDto
import com.wenubey.data.remote.dto.CharacterPageDto
import com.wenubey.data.remote.dto.EpisodeDto
import com.wenubey.data.remote.dto.LocationDto
import com.wenubey.data.remote.dto.LocationPageDto

interface RickAndMortyApi {
    suspend fun getCharacter(id: Int): Result<CharacterDto>
    suspend fun getCharacterPage(pageNumber: Int): Result<CharacterPageDto>
    suspend fun searchCharacter(pageNumber: Int, searchQuery: String): Result<CharacterPageDto>
    suspend fun getCharacters(characterIds: List<Int>): Result<List<CharacterDto>>
    suspend fun getEpisode(id: Int): Result<EpisodeDto>
    suspend fun getEpisodes(ids: List<Int>): Result<List<EpisodeDto>>
    suspend fun getLocation(id: Int): Result<LocationDto>
    suspend fun getLocationPage(pageNumber: Int): Result<LocationPageDto>
    suspend fun searchLocation(pageNumber: Int, searchQuery: String, searchParameter: String? = null): Result<LocationPageDto>
}