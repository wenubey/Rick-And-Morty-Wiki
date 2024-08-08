package com.wenubey.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface RemoteKeyDao {
    @Upsert
    suspend fun insertAll(remoteKeysEntity: List<RemoteKeysEntity>)

    @Query("SELECT * FROM remote_keys WHERE repoId = :repoId")
    suspend fun remoteKeysRepoId(repoId: Int): RemoteKeysEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM remote_keys ORDER BY repoId DESC LIMIT 1")
    suspend fun getLastRemoteKey(): RemoteKeysEntity?
}