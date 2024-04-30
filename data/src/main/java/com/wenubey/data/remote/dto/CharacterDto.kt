package com.wenubey.data.remote.dto

import com.wenubey.data.local.CharacterEntity
import com.wenubey.data.local.LocationEntity
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: LocationResponse,
    val location: LocationResponse,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
) {

    fun toCharacterEntity(
        locationEntity: LocationEntity,
        originEntity: LocationEntity,
    ): CharacterEntity {
        return CharacterEntity(
            created = created,
            episode = episode,
            gender = gender,
            id = id,
            image = image,
            locationEntity = locationEntity,
            name = name,
            originEntity = originEntity,
            species = species,
            status = status,
            type = type,
            url = url
        )
    }

}



