package android.util

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf

object ApiUtil {
    fun MockRequestHandleScope.handleRequest(request: HttpRequestData): HttpResponseData {
        return when (request.url.fullPath) {
            "$CHARACTER_ENDPOINT/1" -> {
                respond(
                    content = "character/character.json".readFile(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        "application/json"
                    )
                )
            }

            "$CHARACTER_ENDPOINT/?page=1" -> {
                respond(
                    content = "character/characterPage.json".readFile(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        "application/json"
                    )
                )
            }

            "$CHARACTER_ENDPOINT/?page=1&name=Rick Sanchez" -> {
                respond(
                    content = "character/searchCharacter.json".readFile(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        "application/json"
                    )
                )
            }

            "$CHARACTER_ENDPOINT/1,2" -> {
                respond(
                    content = "character/getCharacters.json".readFile(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        "application/json"
                    )
                )
            }

            "$EPISODE_ENDPOINT/51" -> {
                respond(
                    content = "episode/episode.json".readFile(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        "application/json"
                    )
                )
            }

            "$EPISODE_ENDPOINT/1,2" -> {
                respond(
                    content = "episode/episodes.json".readFile(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        "application/json"
                    )
                )
            }

            "$LOCATION_ENDPOINT/1" -> {
                respond(
                    content = "location/location.json".readFile(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        "application/json"
                    )
                )
            }

            "$LOCATION_ENDPOINT/?page=1" -> {
                respond(
                    content = "location/locationPage.json".readFile(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        "application/json"
                    )
                )
            }

            "$LOCATION_ENDPOINT/1,2" -> {
                respond(
                    content = "location/locations.json".readFile(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        "application/json"
                    )
                )
            }

            "$LOCATION_ENDPOINT/?page=1&dimension=Dimension C-137" -> {

                respond(
                    content = "location/searchLocationWithParameter.json".readFile(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        "application/json"
                    ),
                )
            }

            "$LOCATION_ENDPOINT/?page=1&name=Abadango" -> {
                respond(
                    content = "location/searchLocationWithoutParameter.json".readFile(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        "application/json"
                    )
                )
            }

            else -> error("Error: ${request.url.fullPath}")
        }
    }


    private const val CHARACTER_ENDPOINT = "/character"
    private const val EPISODE_ENDPOINT = "/episode"
    private const val LOCATION_ENDPOINT = "/location"

}