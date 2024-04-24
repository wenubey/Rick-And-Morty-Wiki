package com.wenubey.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterPageDto(
    val info: Info,
    val results: List<CharacterDto>
) {
    @Serializable
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}


