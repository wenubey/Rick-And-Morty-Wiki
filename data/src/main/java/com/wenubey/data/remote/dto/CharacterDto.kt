package com.wenubey.data.remote.dto

import com.wenubey.domain.model.Character
import com.wenubey.domain.model.CharacterGender
import com.wenubey.domain.model.Episode
import com.wenubey.domain.model.Location
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
    val characterGender = when (gender.lowercase()) {
        "female" -> CharacterGender.Female
        "male" -> CharacterGender.Male
        "genderless" -> CharacterGender.Genderless
        else -> CharacterGender.Unknown
    }

    fun toCharacter(
        location: Location?,
        origin: Location?,
        episode: List<Episode>?
    ): Character {
        return Character(
            created = created,
            episodes = episode ?: listOf(),
            gender = characterGender,
            id = id,
            imageUrl = image,
            location= location ?: Location.default(),
            name = name,
            origin = origin ?: Location.default(),
            species = species,
            status = status,
            type = type,
            url = url
        )
    }

}



