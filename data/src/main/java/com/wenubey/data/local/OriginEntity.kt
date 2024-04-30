package com.wenubey.data.local


import com.wenubey.domain.model.Origin
import kotlinx.serialization.Serializable

@Serializable
data class OriginEntity(
    val id: Int,
    val name: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String,
) {
    fun toDomainOrigin(): Origin {
        return Origin(
            id = id,
            name = name,
            dimension = dimension,
            residents = residents,
            url = url,
            created = created
        )
    }
}
