package com.wenubey.domain.repository

import com.wenubey.domain.model.Episode

interface EpisodeRepository {

    suspend fun getEpisode(id: Int): Result<Episode>

    suspend fun getEpisodes(ids: List<Int>): Result<List<Episode>>

}