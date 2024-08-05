package com.wenubey.data

import com.wenubey.data.remote.dto.CharacterDto
import com.wenubey.data.remote.dto.CharacterPageDto
import com.wenubey.data.remote.dto.EpisodeDto
import com.wenubey.data.remote.dto.LocationDto
import com.wenubey.data.remote.dto.LocationPageDto
import com.wenubey.domain.RickAndMortyApi
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.CharacterPage
import com.wenubey.domain.model.Episode
import com.wenubey.domain.model.Location
import com.wenubey.domain.model.LocationPage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.encodedPath
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class RickAndMortyApiClient @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val httpClient: HttpClient,
) : RickAndMortyApi {

    override suspend fun getCharacter(id: Int): Result<Character> = withContext(ioDispatcher) {
        safeApiCall {
            val characterDto = httpClient.get {
                url {
                    encodedPath = "character/$id"
                }
            }.body<CharacterDto>()

            val location = fetchCharacterLocation(characterDto.location.url)
            val origin = fetchCharacterLocation(characterDto.origin.url)
            val episodes = fetchCharacterEpisodes(characterDto.episode)
            characterDto.toCharacter(location, origin, episodes)

        }
    }



    override suspend fun getCharacterPage(pageNumber: Int): Result<CharacterPage> =
        withContext(ioDispatcher) {
            safeApiCall {
                httpClient.get {
                    url {
                        encodedPath = "character/?page=$pageNumber"
                    }
                }.body<CharacterPageDto>()
                    .toCharacterPage()
            }
        }

    override suspend fun searchCharacter(
        pageNumber: Int,
        searchQuery: String
    ): Result<CharacterPage> =
        withContext(ioDispatcher) {
            safeApiCall {
                httpClient.get {
                    url {
                        encodedPath = "character/?page=$pageNumber&name=$searchQuery"
                    }
                }.body<CharacterPageDto>()
                    .toCharacterPage()
            }
        }

    override suspend fun getCharacters(characterIds: List<Int>): Result<List<Character>> =
        withContext(ioDispatcher) {
            safeApiCall {
                httpClient.get {
                    url {
                        encodedPath = "character/${characterIds.joinToString(separator = ",")}"
                    }
                }.body<List<CharacterDto>>()
                    .map { it.toCharacter(null,null,null) }
            }
        }

    override suspend fun getEpisode(id: Int): Result<Episode> =
        withContext(ioDispatcher) {
            safeApiCall {
                val episodeDto = httpClient.get {
                    url {
                        encodedPath = "episode/$id"
                    }
                }.body<EpisodeDto>()

                val characters = getCharacters(episodeDto.characters.getIdFromUrls()).getOrNull() ?: listOf()

                episodeDto.toEpisode(characters)
            }
        }

    override suspend fun getEpisodes(ids: List<Int>): Result<List<Episode>> =
        withContext(ioDispatcher) {
            safeApiCall {
                val episodesDto = httpClient.get {
                    url {
                        encodedPath = "episode/${ids.joinToString(",")}"
                    }
                }.body<List<EpisodeDto>>()

                val characters = episodesDto.map { getCharacters(it.characters.getIdFromUrls()).getOrNull() ?: listOf() }

                episodesDto.map { it.toEpisode(characters[episodesDto.indexOf(it)]) }

            }
        }

    override suspend fun getLocation(id: Int): Result<Location> =
        withContext(ioDispatcher) {
            safeApiCall {
               val locationDto =  httpClient.get {
                    url {
                        encodedPath = "location/$id"
                    }
                }.body<LocationDto>()

                val residents = fetchLocationResidents(locationDto.residents.getIdFromUrls())
                locationDto.toLocation(residents)
            }
        }

    override suspend fun getLocationPage(pageNumber: Int): Result<LocationPage> =
        withContext(ioDispatcher) {
            safeApiCall {
                httpClient.get {
                    url {
                        encodedPath = "location/?page=$pageNumber"
                    }
                }.body<LocationPageDto>()
                    .toLocationPage()
            }
        }

    override suspend fun searchLocation(
        pageNumber: Int,
        searchQuery: String,
        searchParameter: String?,
    ): Result<LocationPage> =
        withContext(ioDispatcher) {
            safeApiCall {
                httpClient.get {
                    url {
                        encodedPath = if (searchParameter != null) {
                            "location/?page=$pageNumber&$searchParameter=$searchQuery"
                        } else {
                            "location/?page=$pageNumber&name=$searchQuery"
                        }
                    }
                }.body<LocationPageDto>()
                    .toLocationPage()
            }
        }

    private suspend fun fetchLocationResidents(characterIds: List<Int>): List<Character> = withContext(ioDispatcher) {
        getCharacters(characterIds).getOrNull() ?: listOf()
    }

    private suspend fun fetchCharacterLocation(locationUrl: String): Location =
        withContext(ioDispatcher) {
            getLocation(locationUrl.getIdFromUrl()).getOrNull() ?: Location.default()
        }

    private suspend fun fetchCharacterEpisodes(episodeUrls: List<String>): List<Episode> =
        withContext(ioDispatcher) {
            getEpisodes(episodeUrls.getIdFromUrls()).getOrNull() ?: listOf()
        }

    private inline fun <T> safeApiCall(apiCall: () -> T): Result<T> {
        return try {
            Timber.d("safeApiCall:SUCCESS")
            Result.success(apiCall())
        } catch (e: Exception) {
            Timber.e(e, "safeApiCall:ERROR: ")
            Result.failure(exception = e)
        }
    }
}