package com.wenubey.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wenubey.data.getIdFromUrl
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.CharacterGender
import kotlinx.serialization.Serializable

@Entity(tableName = TABLE_NAME)
@Serializable
data class CharacterEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "created")
    val created: String,
    @ColumnInfo(name = "episode")
    val episode: List<String>,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "locationEntity")
    val locationEntity: LocationEntity,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "originEntity")
    val originEntity: LocationEntity,
    @ColumnInfo(name = "species")
    val species: String,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "url")
    val url: String,
    )

fun CharacterEntity.toDomainCharacter(): Character {
    val characterGender = when (gender.lowercase()) {
        "female" -> CharacterGender.Female
        "male" -> CharacterGender.Male
        "genderless" -> CharacterGender.Genderless
        else -> CharacterGender.Unknown
    }
    return Character(
        created = created,
        episodeIds = episode.map { it.getIdFromUrl() },
        gender = characterGender,
        id = id,
        imageUrl = image,
        location = locationEntity.toDomainLocation(),
        name = name,
        origin = originEntity.toDomainLocation(),
        species = species,
        status = status,
        type = type
    )
}

const val TABLE_NAME = "characters"
