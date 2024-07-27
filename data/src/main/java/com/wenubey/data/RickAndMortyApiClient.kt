package com.wenubey.data

import android.util.Log
import com.wenubey.data.remote.dto.CharacterDto
import com.wenubey.data.remote.dto.CharacterPageDto
import com.wenubey.data.remote.dto.EpisodeDto
import com.wenubey.data.remote.dto.LocationDto
import com.wenubey.data.remote.dto.LocationPageDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.encodedPath
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RickAndMortyApiClient @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val httpClient: HttpClient,
) : RickAndMortyApi {

    override suspend fun getCharacter(id: Int): Result<CharacterDto> = withContext(ioDispatcher) {
        safeApiCall {
            httpClient.get {
                url {
                    encodedPath = "character/$id"
                }
            }.body<CharacterDto>()
        }
    }

    override suspend fun getCharacterPage(pageNumber: Int): Result<CharacterPageDto> =
        withContext(ioDispatcher) {
            safeApiCall {
                httpClient.get {
                    url {
                        encodedPath = "character/?page=$pageNumber"
                    }
                }.body<CharacterPageDto>()
            }
        }

    override suspend fun searchCharacter(
        pageNumber: Int,
        searchQuery: String
    ): Result<CharacterPageDto> =
        withContext(ioDispatcher) {
            safeApiCall {
                httpClient.get {
                    url {
                        encodedPath = "character/?page=$pageNumber&name=$searchQuery"
                    }
                }.body<CharacterPageDto>()
            }
        }

    override suspend fun getCharacters(characterIds: List<Int>): Result<List<CharacterDto>> =
        withContext(ioDispatcher) {
            safeApiCall {
                httpClient.get {
                    url {
                        encodedPath = "character/${characterIds.joinToString(separator = ",")}"
                    }
                }.body<List<CharacterDto>>()
            }
        }

    override suspend fun getEpisode(id: Int): Result<EpisodeDto> =
        withContext(ioDispatcher) {
            safeApiCall {
                httpClient.get {
                    url {
                        encodedPath = "episode/$id"
                    }
                }.body<EpisodeDto>()
            }
        }

    override suspend fun getEpisodes(ids: List<Int>): Result<List<EpisodeDto>> =
        withContext(ioDispatcher) {
            safeApiCall {
                httpClient.get {
                    url {
                        encodedPath = "episode/${ids.joinToString(",")}"
                    }
                }.body<List<EpisodeDto>>()
            }
        }

    override suspend fun getLocation(id: Int): Result<LocationDto> =
        withContext(ioDispatcher) {
            safeApiCall {
                httpClient.get {
                    url {
                        encodedPath = "location/$id"
                    }
                }.body<LocationDto>()
            }
        }

    override suspend fun getLocationPage(pageNumber: Int): Result<LocationPageDto> =
        withContext(ioDispatcher) {
            safeApiCall {
                httpClient.get {
                    url {
                        encodedPath = "location/?page=$pageNumber"
                    }
                }.body<LocationPageDto>()
            }
        }

    override suspend fun searchLocation(
        pageNumber: Int,
        searchQuery: String,
        searchParameter: String?,
    ): Result<LocationPageDto> =
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
            }
        }

    private inline fun <T> safeApiCall(apiCall: () -> T): Result<T> {
        return try {
            Log.d(TAG, "safeApiCall:SUCCESS")
            Result.success(apiCall())
        } catch (e: Exception) {
            Log.e(TAG, "safeApiCall:ERROR: ", e)
            Result.failure(exception = e)
        }
    }


    private companion object {
        const val TAG = "RickAndMortyApiClient"
    }
}