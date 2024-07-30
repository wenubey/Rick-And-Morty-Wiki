package com.wenubey.domain.model

import androidx.room.Entity
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = EPISODE_TABLE_NAME)
data class Episode(
    val id: Int,
    val name: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val airDate: String,
    val charactersInEpisode: List<Character>
)

const val EPISODE_TABLE_NAME = "episodes"