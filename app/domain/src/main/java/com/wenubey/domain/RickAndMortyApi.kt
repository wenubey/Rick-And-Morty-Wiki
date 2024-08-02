package com.wenubey.domain

import com.wenubey.domain.model.Character
import com.wenubey.domain.model.CharacterPage
import com.wenubey.domain.model.Episode
import com.wenubey.domain.model.Location
import com.wenubey.domain.model.LocationPage

interface RickAndMortyApi {
    suspend fun getCharacter(id: Int): Result<Character>
    suspend fun getCharacterPage(pageNumber: Int): Result<CharacterPage>
    suspend fun searchCharacter(pageNumber: Int, searchQuery: String): Result<CharacterPage>
    suspend fun getCharacters(characterIds: List<Int>): Result<List<Character>>
    suspend fun getEpisode(id: Int): Result<Episode>
    suspend fun getEpisodes(ids: List<Int>): Result<List<Episode>>
    suspend fun getLocation(id: Int): Result<Location>
    suspend fun getLocationPage(pageNumber: Int): Result<LocationPage>
    suspend fun searchLocation(pageNumber: Int, searchQuery: String, searchParameter: String? = null): Result<LocationPage>
}