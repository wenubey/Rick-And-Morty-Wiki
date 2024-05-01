package com.wenubey.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LocationPageDto(
    val info: LocationInfo,
    val results: List<LocationDto>
) {
    @Serializable
    data class LocationInfo(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}


