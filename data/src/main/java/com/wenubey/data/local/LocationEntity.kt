package com.wenubey.data.local

import com.wenubey.domain.model.Location
import kotlinx.serialization.Serializable


@Serializable
data class LocationEntity(
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
