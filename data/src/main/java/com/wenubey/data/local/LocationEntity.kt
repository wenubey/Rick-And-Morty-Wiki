package com.wenubey.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wenubey.domain.model.Location
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = LOCATION_TABLE_NAME)
data class LocationEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String,
    val type: String,
) {
    fun toDomainLocation(): Location {
        return Location(
            id = id,
            name = name,
            dimension = dimension,
            residents = residents,
            url = url,
            created = created,
            type = type,
        )
    }

}
const val LOCATION_TABLE_NAME = "locations"

