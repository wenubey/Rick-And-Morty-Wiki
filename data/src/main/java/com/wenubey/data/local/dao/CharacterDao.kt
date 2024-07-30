package com.wenubey.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wenubey.domain.model.Character

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<Character>)

    @Query("SELECT * FROM characters")
    fun pagingSource(): PagingSource<Int, Character>

    @Query("DELETE FROM characters")
    suspend fun clearAll()
}