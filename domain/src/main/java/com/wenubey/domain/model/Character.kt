package com.wenubey.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = CHARACTER_TABLE_NAME)
@Serializable
data class Character(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "created")
    val created: String,
    @ColumnInfo(name = "episodes")
    val episodes: List<Episode>,
    @ColumnInfo(name = "gender")
    val gender: CharacterGender,
    @ColumnInfo(name = "image")
    val imageUrl: String,
    @ColumnInfo(name = "locationEntity")
    val location: Location,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "originEntity")
    val origin: Location,
    @ColumnInfo(name = "species")
    val species: String,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "url")
    val url: String,
) {

    companion object {
        fun default() = Character(
            created = "created",
            episodes = listOf(),
            gender = CharacterGender.Male,
            id = 0,
            imageUrl = "",
            location = Location.default(),
            name = "Abadango Cluster",
            origin = Location.default(),
            species = "Human",
            status = "Unknown",
            type = "",
            url = ""
        )
    }


}

const val CHARACTER_TABLE_NAME = "characters"