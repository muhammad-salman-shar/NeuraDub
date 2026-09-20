package com.neurasamu.build.neura_dub.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SceneDao {
    @Query("SELECT * FROM scenes WHERE projectId = :projectId ORDER BY orderIndex ASC")
    fun observeByProject(projectId: String): Flow<List<SceneEntity>>

    @Query("SELECT COALESCE(MAX(orderIndex), -1) + 1 FROM scenes WHERE projectId = :projectId")
    suspend fun nextOrderIndex(projectId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(scene: SceneEntity)

    @Query("DELETE FROM scenes WHERE id = :id")
    suspend fun deleteById(id: String)
}
