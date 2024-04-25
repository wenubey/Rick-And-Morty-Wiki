package com.wenubey.data

import android.util.Log
import com.wenubey.data.local.toDomainCharacter
import com.wenubey.data.remote.dto.CharacterDto
import com.wenubey.data.remote.dto.CharacterPageDto
import com.wenubey.data.remote.dto.EpisodeDto
import com.wenubey.data.remote.dto.toCharacterEntity
import com.wenubey.data.remote.dto.toDomainEpisode
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Episode
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


    //TODO fix this issue add dao's insert one delete one
    // I will start to fetch cached values on repo
    suspend fun getCharacter(id: Int): Result<Character> {
        return safeApiCall {
            client.get("character/$id")
                .body<CharacterDto>()
                .toCharacterEntity()
                .toDomainCharacter()
        }
    }

    suspend fun getCharacterPage(pageNumber: Int): CharacterPageDto {
        return client.get("character/?page=$pageNumber")
                .body<CharacterPageDto>()

    }

    suspend fun searchCharacter(name: String): CharacterPageDto {
        return client.get("character/?name=$name")
                .body<CharacterPageDto>()
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
        const val TAG = "ktorClient"
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}