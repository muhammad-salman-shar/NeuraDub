package com.neurasamu.build.neura_dub.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LineDao {
    @Query("SELECT * FROM lines WHERE sceneId = :sceneId ORDER BY orderIndex ASC")
    fun observeByScene(sceneId: String): Flow<List<LineEntity>>

    @Query("SELECT COALESCE(MAX(orderIndex), -1) + 1 FROM lines WHERE sceneId = :sceneId")
    suspend fun nextOrderIndex(sceneId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(line: LineEntity)

    @Query("DELETE FROM lines WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM lines WHERE sceneId IN (SELECT id FROM scenes WHERE projectId = :projectId)")
    fun observeTotalByProject(projectId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM lines WHERE status = :status AND sceneId IN (SELECT id FROM scenes WHERE projectId = :projectId)")
    fun observeCountByStatus(projectId: String, status: String): Flow<Int>
}
