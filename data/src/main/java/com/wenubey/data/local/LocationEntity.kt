package com.wenubey.data.local

import com.wenubey.domain.model.Location
import kotlinx.serialization.Serializable


@Serializable
data class LocationEntity(
    val id: Int,
    val name: String,
    val dimension: String,
    val residents: List<Int>,
    val url: String,
    val created: String,
) {
    fun toDomainLocation(): Location {
        return Location(
            id = id,
            name = name,
            dimension = dimension,
            residents = residents,
            url = url,
            created = created
        )
    }
}
