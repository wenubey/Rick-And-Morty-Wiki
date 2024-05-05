package com.wenubey.domain.repository

import androidx.paging.PagingData
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun getLocation(id: Int): Result<Location>

    suspend fun getLocationPage(): Flow<PagingData<Location>>

    suspend fun getCharactersById(ids: List<Int>): Result<List<Character>>
}