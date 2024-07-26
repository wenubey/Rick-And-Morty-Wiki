package com.wenubey.data.repository

import com.wenubey.data.RickAndMortyApi
import com.wenubey.domain.model.Episode
import com.wenubey.domain.repository.EpisodeRepository
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi
): EpisodeRepository {

    override suspend fun getEpisode(id: Int): Result<Episode> {
        TODO("Not yet implemented")
    }

    override suspend fun getEpisodes(ids: List<Int>): Result<List<Episode>> {
        TODO("Not yet implemented")
    }
}