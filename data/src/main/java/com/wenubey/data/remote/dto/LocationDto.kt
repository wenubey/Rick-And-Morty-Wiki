package com.wenubey.data.remote.dto

import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Location
import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val name: String,
    val url: String
)

@Serializable
data class LocationDto(
    val id: Int,
    val name: String,
    val dimension: String,
    val residents: List<String>,
    val type: String,
    val url: String,
    val created: String,
) {
    fun toLocation(
        residents: List<Character>?
    ): Location {
        return Location(
            id = id,
            name = name,
            dimension = dimension,
            residents = residents ?: listOf(),
            url = url,
            created = created,
            type = type,
            population = residents?.size ?: this.residents.size
        )
    }

    companion object {
        fun default(): LocationDto {
            return LocationDto(
                id = -1,
                name = "",
                dimension = "",
                url = "",
                residents = listOf(),
                created = "",
                type = ""
            )
        }
    }
}


