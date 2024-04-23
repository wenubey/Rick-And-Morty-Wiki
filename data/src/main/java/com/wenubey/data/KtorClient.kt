package com.wenubey.data

import com.wenubey.domain.model.Character
import com.wenubey.domain.model.CharacterPage
import com.wenubey.domain.model.Episode
import com.wenubey.data.dto.CharacterDto
import com.wenubey.data.dto.CharacterPageDto
import com.wenubey.data.dto.EpisodeDto
import com.wenubey.data.dto.toDomainCharacter
import com.wenubey.data.dto.toDomainCharacterPage
import com.wenubey.data.dto.toDomainEpisode
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

    suspend fun getCharacter(id: Int): Result<Character> {
        characterCache[id]?.let { return Result.success(it) }
        return safeApiCall {
            client.get("character/$id")
                .body<CharacterDto>()
                .toDomainCharacter()
                .also { characterCache[id] = it }
        }
    }

    suspend fun getCharacterPage(pageNumber: Int): Result<CharacterPage> {
        return safeApiCall {
            client.get("character/?page=$pageNumber")
                .body<CharacterPageDto>()
                .toDomainCharacterPage()
        }
    }

    suspend fun getEpisode(id: Int): Result<Episode> {
        return safeApiCall {
            client.get("episode/$id")
                .body<EpisodeDto>()
                .toDomainEpisode()
        }
    }

    suspend fun getEpisodes(ids: List<Int>): Result<List<Episode>> {
        return if (ids.size == 1) {
            getEpisode(ids[0]).map { listOf(it) }
        } else {
            val idsCommaSeparated = ids.joinToString(separator = ",")
            safeApiCall {
                client.get("episode/$idsCommaSeparated")
                    .body<List<EpisodeDto>>()
                    .map { it.toDomainEpisode() }
            }
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): Result<T> {
        return try {
            Result.success(apiCall())
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    private companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}