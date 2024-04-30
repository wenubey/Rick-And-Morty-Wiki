package com.wenubey.data.remote.dto

import com.wenubey.data.local.OriginEntity
import kotlinx.serialization.Serializable

@Serializable
data class OriginResponse(
    val name: String,
    val url: String
)

@Serializable
data class OriginDto(
    val id: Int,
    val name: String,
    val dimension: String,
    val residents: List<String>,
    val url: String?,
    val created: String,
) {
    fun toOriginEntity(): OriginEntity {
        return OriginEntity(
            id = id,
            name = name,
            dimension = dimension,
            residents = residents,
            url = url ?: "",
            created = created
        )
    }

    companion object {
        fun default(): OriginDto {
            return OriginDto(
                id = -1,
                name = "",
                dimension = "",
                url = "",
                residents = listOf(),
                created = ""
            )
        }
    }
}