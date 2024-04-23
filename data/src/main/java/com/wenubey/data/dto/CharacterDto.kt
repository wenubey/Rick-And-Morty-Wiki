package com.wenubey.data.dto

import com.wenubey.domain.model.Character
import com.wenubey.domain.model.CharacterGender
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

fun CharacterDto.toDomainCharacter(): Character {
    val characterGender = when (gender.lowercase()) {
        "female" -> CharacterGender.Female
        "male" -> CharacterGender.Male
        "genderless" -> CharacterGender.Genderless
        else -> CharacterGender.Unknown
    }
    return Character(
        created = created,
        episodeIds = episode.map { it.substring(it.lastIndexOf("/") + 1).toInt() },
        gender = characterGender,
        id = id,
        imageUrl = image,
        location = Character.Location(
            name = location.name,
            url = location.url
        ),
        name = name,
        origin = Character.Origin(name = origin.name, url = origin.url),
        species = species,
        status = status,
        type = type
    )
}
