package com.wenubey.network.models.remote

import com.wenubey.network.models.domain.CharacterPage
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

fun CharacterPageDto.toDomainCharacterPage(): CharacterPage {
    return CharacterPage(
        info = CharacterPage.Info(
            count = info.count,
            pages = info.pages,
            next = info.next,
            prev = info.next
        ),
        characters = results.map { it.toDomainCharacter() }
    )
}

