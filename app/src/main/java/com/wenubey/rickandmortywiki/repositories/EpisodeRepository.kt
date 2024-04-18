package com.wenubey.rickandmortywiki.repositories

import com.wenubey.network.models.ApiOperation
import com.wenubey.network.models.domain.Episode

interface EpisodeRepository {

    suspend fun getEpisode(id: Int): ApiOperation<Episode>

    suspend fun getEpisodes(ids: List<Int>): ApiOperation<List<Episode>>

}