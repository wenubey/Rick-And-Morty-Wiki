package com.wenubey.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wenubey.domain.model.Location

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<Location>)

    @Query("SELECT * FROM locations")
    fun pagingSource(): PagingSource<Int, Location>

    @Query("DELETE FROM locations")
    suspend fun clearAll()
}