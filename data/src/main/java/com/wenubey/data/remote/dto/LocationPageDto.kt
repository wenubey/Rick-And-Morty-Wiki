package com.wenubey.data.remote.dto

import com.wenubey.domain.model.LocationPage
import kotlinx.serialization.Serializable

@Serializable
data class LocationPageDto(
    val info: InfoDto,
    val results: List<LocationDto>
) {
    @Serializable
    data class InfoDto(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    ) {
        fun toInfo(): LocationPage.LocationInfo =
            LocationPage.LocationInfo(
                count = count,
                pages = pages,
                next = next,
                prev = prev
            )
    }

    fun toLocationPage(): LocationPage {
        return   LocationPage(
            info = info.toInfo(),
            results = results.map { it.toLocation(null) }
        )
    }

}


