package com.wenubey.data.remote.dto

import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Episode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeDto(
    val id: Int,
    val name: String,
    val episode: String,
    @SerialName("air_date")
    val airDate: String,
    val characters: List<String>
) {
    fun toEpisode(
        characters: List<Character>
    ): Episode {
        return Episode(
            id = id,
            name = name,
            seasonNumber = episode.filter { it.isDigit() }.take(2).toInt(),
            episodeNumber = episode.filter { it.isDigit() }.takeLast(2).toInt(),
            airDate = airDate,
            charactersInEpisode = characters
        )
    }
}

