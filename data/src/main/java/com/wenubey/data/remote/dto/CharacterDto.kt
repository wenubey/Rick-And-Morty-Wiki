package com.wenubey.data.remote.dto

import com.wenubey.data.local.CharacterEntity
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
) {
    @Serializable
    data class Location(
        val name: String,
        val url: String
    )

    @Serializable
    data class Origin(
        val name: String,
        val url: String
    )
}

fun CharacterDto.toCharacterEntity(): CharacterEntity {
    return CharacterEntity(
        created = created,
        episode = episode,
        gender = gender,
        id = id,
        image = image,
        locationName = location.name,
        locationUrl = location.url,
        name = name,
        originName = origin.name,
        originUrl= origin.url,
        species = species,
        status = status,
        type = type,
        url = url
    )
}
