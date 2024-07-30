package com.wenubey.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity(tableName = LOCATION_TABLE_NAME)
@Serializable
data class Location(
    @PrimaryKey
    val id: Int,
    val name: String,
    val dimension: String,
    val residents: List<Character>,
    val url: String,
    val created: String,
    val type:String,
    val population: Int
) {

    companion object {
        fun default(): Location {
            return Location(
                id = 1,
                name = "Earth",
                dimension = "Earth Dimension",
                residents = listOf(),
                url = "",
                created = "",
                type = "",
                population = 0
            )
        }
    }
}

const val LOCATION_TABLE_NAME = "locations"