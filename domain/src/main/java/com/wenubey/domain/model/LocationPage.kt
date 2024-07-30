package com.wenubey.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationPage(
    val info: LocationInfo,
    val results: List<Location>
) {
    @Serializable
    data class LocationInfo(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}
