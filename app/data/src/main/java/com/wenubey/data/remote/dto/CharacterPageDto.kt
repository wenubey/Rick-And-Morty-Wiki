package com.wenubey.data.remote.dto

import com.wenubey.domain.model.CharacterPage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterPageDto(
    @SerialName("info")
    val infoDto: InfoDto,
    val results: List<CharacterDto>
) {
    @Serializable
    data class InfoDto(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    ) {
        fun toInfo(): CharacterPage.Info = CharacterPage.Info(
            count = count,
            pages = pages,
            next = next,
            prev = prev
        )
    }

    fun toCharacterPage(): CharacterPage =
        CharacterPage(
            info = infoDto.toInfo(),
            results = results.map { it.toCharacter(null, null, null) }
        )
}


