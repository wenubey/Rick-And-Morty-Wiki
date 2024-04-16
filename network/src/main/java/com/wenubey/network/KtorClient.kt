package com.wenubey.network

import com.wenubey.network.models.ApiOperation
import com.wenubey.network.models.domain.Character
import com.wenubey.network.models.domain.CharacterPage
import com.wenubey.network.models.domain.Episode
import com.wenubey.network.models.remote.CharacterDto
import com.wenubey.network.models.remote.CharacterPageDto
import com.wenubey.network.models.remote.EpisodeDto
import com.wenubey.network.models.remote.toDomainCharacter
import com.wenubey.network.models.remote.toDomainCharacterPage
import com.wenubey.network.models.remote.toDomainEpisode
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {
    private val client = HttpClient(OkHttp) {
        defaultRequest { url(BASE_URL) }

        install(Logging) {
            logger = Logger.SIMPLE
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private var characterCache = mutableMapOf<Int, Character>()

    suspend fun getCharacter(id: Int): ApiOperation<Character> {
        characterCache[id]?.let { return ApiOperation.Success(it) }
        return safeApiCall {
            client.get("character/$id")
                .body<CharacterDto>()
                .toDomainCharacter()
                .also { characterCache[id] = it }
        }
    }

    suspend fun getCharacterPage(pageNumber: Int): ApiOperation<CharacterPage> {
        return safeApiCall {
            client.get("character/?page=$pageNumber")
                .body<CharacterPageDto>()
                .toDomainCharacterPage()
        }
    }

    suspend fun getEpisode(id: Int): ApiOperation<Episode> {
        return safeApiCall {
            client.get("episode/$id")
                .body<EpisodeDto>()
                .toDomainEpisode()
        }
    }

    suspend fun getEpisodes(ids: List<Int>): ApiOperation<List<Episode>> {
        return if (ids.size == 1) {
            getEpisode(ids[0]).mapSuccess {
                listOf(it)
            }
        } else {
            val idsCommaSeparated = ids.joinToString(separator = ",")
            safeApiCall {
                client.get("episode/$idsCommaSeparated")
                    .body<List<EpisodeDto>>()
                    .map { it.toDomainEpisode() }
            }
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T> {
        return try {
            ApiOperation.Success(data = apiCall())
        } catch (e: Exception) {
            ApiOperation.Failure(exception = e)
        }
    }

    private companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}