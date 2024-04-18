package com.wenubey.rickandmortywiki.repositories

import com.wenubey.network.KtorClient
import com.wenubey.network.models.ApiOperation
import com.wenubey.network.models.domain.Episode
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val ktorClient: KtorClient
): EpisodeRepository {

    override suspend fun getEpisode(id: Int): ApiOperation<Episode> {
        return ktorClient.getEpisode(id)
    }

    override suspend fun getEpisodes(ids: List<Int>): ApiOperation<List<Episode>> {
        return ktorClient.getEpisodes(ids)
    }
}