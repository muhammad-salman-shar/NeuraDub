package com.neurasamu.build.neura_dub.data.repository

import com.neurasamu.build.neura_dub.data.local.ProjectDao
import com.neurasamu.build.neura_dub.data.local.ProjectEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(
    private val dao: ProjectDao
) {
    fun observeAll(): Flow<List<ProjectEntity>> = dao.observeAll()

    fun observeById(id: String): Flow<ProjectEntity?> = dao.observeById(id)

    suspend fun create(
        title: String,
        language: String,
        framerateMilli: Int,
        loudnessTarget: String,
        licenseConfirmed: Boolean
    ): String {
        val now = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()
        dao.upsert(
            ProjectEntity(
                id = id,
                title = title.trim(),
                language = language,
                framerateMilli = framerateMilli,
                loudnessTarget = loudnessTarget,
                licenseConfirmed = licenseConfirmed,
                status = ProjectEntity.STATUS_ACTIVE,
                createdAt = now,
                updatedAt = now
            )
        )
        return id
    }

    suspend fun delete(id: String) = dao.deleteById(id)
}
