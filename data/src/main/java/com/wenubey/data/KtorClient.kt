package com.wenubey.data

import android.util.Log
import com.wenubey.data.remote.dto.CharacterDto
import com.wenubey.data.remote.dto.CharacterPageDto
import com.wenubey.data.remote.dto.EpisodeDto
import com.wenubey.data.remote.dto.LocationDto
import com.wenubey.data.remote.dto.LocationPageDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {
    private val client = HttpClient(OkHttp) {
        defaultRequest { url(BASE_URL) }

        install(Logging) {
            logger = Logger.ANDROID
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }


    suspend fun getCharacter(id: Int): Result<CharacterDto> {
        return safeApiCall {
            client.get("character/$id")
                .body<CharacterDto>()

        }
    }

    suspend fun getCharacterPage(pageNumber: Int): CharacterPageDto {
        return client.get("character/?page=$pageNumber")
            .body<CharacterPageDto>()
    }

    suspend fun searchCharacter(pageNumber: Int, searchQuery: String): CharacterPageDto {
        return client.get("character/?page=$pageNumber&name=$searchQuery")
            .body<CharacterPageDto>()
    }

    suspend fun getCharacters(characterIds: List<Int>): Result<List<CharacterDto>> {
        return if (characterIds.size == 1) {
            getCharacter(characterIds[0]).map { listOf(it) }
        } else {
            safeApiCall {
                client.get("character/$characterIds")
                    .body<List<CharacterDto>>()
            }
        }
    }

    suspend fun getEpisode(id: Int): Result<EpisodeDto> {
        return safeApiCall {
            client.get("episode/$id")
                .body<EpisodeDto>()

        }
    }

    suspend fun getEpisodes(ids: List<Int>): Result<List<EpisodeDto>> {
        return if (ids.size == 1) {
            getEpisode(ids[0]).map { listOf(it) }
        } else {
            val idsCommaSeparated = ids.joinToString(separator = ",")
            safeApiCall {
                client.get("episode/$idsCommaSeparated")
                    .body<List<EpisodeDto>>()
            }
        }
    }

    suspend fun getLocation(id: Int): Result<LocationDto> {
        return safeApiCall {
            client.get("location/$id")
                .body<LocationDto>()
        }
    }

    suspend fun getLocationPage(pageNumber: Int): Result<LocationPageDto> {
        return safeApiCall {
            client.get("location?page=$pageNumber")
                .body<LocationPageDto>()
        }
    }

    suspend fun searchLocation(pageNumber: Int, searchQuery: String) : Result<LocationPageDto> {
        return safeApiCall {
            val queryParameters = searchQuery.split(",")
            client.get("location/?page=$pageNumber&${queryParameters.first()}=${queryParameters.last()}")
                .body<LocationPageDto>()
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): Result<T> {
        return try {
            Log.w(TAG, "safeApiCall:SUCCESS")
            Result.success(apiCall())
        } catch (e: Exception) {
            Log.e(TAG, "safeApiCall:ERROR: ", e)
            Result.failure(exception = e)
        }
    }


    private companion object {
        const val TAG = "ktorClient"
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}