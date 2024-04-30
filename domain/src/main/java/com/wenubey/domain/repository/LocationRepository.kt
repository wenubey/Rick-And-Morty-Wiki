package com.wenubey.domain.repository

import androidx.paging.PagingData
import com.wenubey.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun getLocation(id: Int): Result<Location>

    suspend fun getLocationPage(name: String?): Flow<PagingData<Location>>

}