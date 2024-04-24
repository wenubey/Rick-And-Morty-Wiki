package com.wenubey.data.repository

import com.wenubey.domain.model.Episode
import com.wenubey.domain.repository.EpisodeRepository
import com.wenubey.data.KtorClient
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val ktorClient: KtorClient
): EpisodeRepository {

    override suspend fun getEpisode(id: Int): Result<Episode> {
        return ktorClient.getEpisode(id)
    }

    override suspend fun getEpisodes(ids: List<Int>): Result<List<Episode>> {
        return ktorClient.getEpisodes(ids)
    }
}