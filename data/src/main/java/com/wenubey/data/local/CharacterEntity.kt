package com.wenubey.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.CharacterGender

@Entity(tableName = TABLE_NAME)
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
    @ColumnInfo(name = "location_name")
    val locationName: String,
    @ColumnInfo(name = "location_url")
    val locationUrl: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "origin_name")
    val originName: String,
    @ColumnInfo(name = "origin_url")
    val originUrl: String,
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
        episodeIds = episode.map { it.substring(it.lastIndexOf("/") + 1).toInt() },
        gender = characterGender,
        id = id,
        imageUrl = image,
        location = Character.Location(
            name = locationName,
            url = locationUrl
        ),
        name = name,
        origin = Character.Origin(name = originName, url = originUrl),
        species = species,
        status = status,
        type = type
    )
}

const val TABLE_NAME = "characters"
