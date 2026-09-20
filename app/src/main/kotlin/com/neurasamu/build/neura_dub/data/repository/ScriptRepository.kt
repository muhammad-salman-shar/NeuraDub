package com.neurasamu.build.neura_dub.data.repository

import com.neurasamu.build.neura_dub.data.local.CharacterDao
import com.neurasamu.build.neura_dub.data.local.CharacterEntity
import com.neurasamu.build.neura_dub.data.local.LineDao
import com.neurasamu.build.neura_dub.data.local.LineEntity
import com.neurasamu.build.neura_dub.data.local.SceneDao
import com.neurasamu.build.neura_dub.data.local.SceneEntity
import com.neurasamu.build.neura_dub.data.local.ScriptText
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScriptRepository @Inject constructor(
    private val characterDao: CharacterDao,
    private val sceneDao: SceneDao,
    private val lineDao: LineDao
) {
    fun observeCharacters(projectId: String): Flow<List<CharacterEntity>> =
        characterDao.observeByProject(projectId)

    fun observeScenes(projectId: String): Flow<List<SceneEntity>> =
        sceneDao.observeByProject(projectId)

    fun observeLines(sceneId: String): Flow<List<LineEntity>> =
        lineDao.observeByScene(sceneId)

    fun observeTotalLines(projectId: String): Flow<Int> =
        lineDao.observeTotalByProject(projectId)

    fun observeLinesByStatus(projectId: String, status: String): Flow<Int> =
        lineDao.observeCountByStatus(projectId, status)

    suspend fun addCharacter(projectId: String, name: String, colorHex: String, voiceType: String): String {
        val id = UUID.randomUUID().toString()
        characterDao.upsert(
            CharacterEntity(
                id = id,
                projectId = projectId,
                name = name.trim(),
                colorHex = colorHex,
                voiceType = voiceType,
                createdAt = System.currentTimeMillis()
            )
        )
        return id
    }

    suspend fun deleteCharacter(id: String) = characterDao.deleteById(id)

    suspend fun addScene(projectId: String, title: String): String {
        val id = UUID.randomUUID().toString()
        val order = sceneDao.nextOrderIndex(projectId)
        sceneDao.upsert(
            SceneEntity(
                id = id,
                projectId = projectId,
                orderIndex = order,
                title = title.trim(),
                createdAt = System.currentTimeMillis()
            )
        )
        return id
    }

    suspend fun deleteScene(id: String) = sceneDao.deleteById(id)

    suspend fun addLine(
        sceneId: String,
        timeInMs: Int,
        timeOutMs: Int,
        markerType: String,
        characterId: String?,
        originalText: String,
        literalText: String,
        adaptedText: String,
        status: String
    ): String {
        val id = UUID.randomUUID().toString()
        val order = lineDao.nextOrderIndex(sceneId)
        val flaps = ScriptText.estimateFlapCount(timeInMs, timeOutMs)
        val syllables = ScriptText.countSyllables(adaptedText.ifBlank { originalText })
        val delta = ScriptText.flapDeltaPercent(syllables, flaps)
        lineDao.upsert(
            LineEntity(
                id = id,
                sceneId = sceneId,
                orderIndex = order,
                timeInMs = timeInMs,
                timeOutMs = timeOutMs,
                markerType = markerType,
                characterId = characterId,
                originalText = originalText.trim(),
                literalText = literalText.trim(),
                adaptedText = adaptedText.trim(),
                syllableCount = syllables,
                flapCount = flaps,
                flapDeltaPct = delta,
                status = status,
                createdAt = System.currentTimeMillis()
            )
        )
        return id
    }

    suspend fun updateLine(line: LineEntity) {
        val flaps = ScriptText.estimateFlapCount(line.timeInMs, line.timeOutMs)
        val syllables = ScriptText.countSyllables(line.adaptedText.ifBlank { line.originalText })
        val delta = ScriptText.flapDeltaPercent(syllables, flaps)
        lineDao.upsert(
            line.copy(
                syllableCount = syllables,
                flapCount = flaps,
                flapDeltaPct = delta
            )
        )
    }

    suspend fun deleteLine(id: String) = lineDao.deleteById(id)
}
