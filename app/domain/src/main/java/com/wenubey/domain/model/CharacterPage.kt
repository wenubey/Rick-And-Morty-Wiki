package com.wenubey.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterPage(
    val info: Info,
    val results: List<Character>
) {
    @Serializable
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}
