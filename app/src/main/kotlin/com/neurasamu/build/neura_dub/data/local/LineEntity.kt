package com.neurasamu.build.neura_dub.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lines",
    foreignKeys = [
        ForeignKey(
            entity = SceneEntity::class,
            parentColumns = ["id"],
            childColumns = ["sceneId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sceneId")]
)
data class LineEntity(
    @PrimaryKey val id: String,
    val sceneId: String,
    val orderIndex: Int,
    val timeInMs: Int,
    val timeOutMs: Int,
    val markerType: String,
    val characterId: String?,
    val originalText: String,
    val literalText: String,
    val adaptedText: String,
    val syllableCount: Int,
    val flapCount: Int,
    val flapDeltaPct: Float,
    val status: String,
    val createdAt: Long
) {
    companion object {
        const val MARKER_SYNC = "SYNC"
        const val MARKER_OFF = "OFF"
        const val MARKER_REACT = "REACT"
        const val MARKER_NARR = "NARR"

        const val STATUS_PENDING = "pending"
        const val STATUS_READY = "ready"
        const val STATUS_RETAKE = "retake"
        const val STATUS_APPROVED = "approved"

        val MARKERS = listOf(MARKER_SYNC, MARKER_OFF, MARKER_REACT, MARKER_NARR)
        val STATUSES = listOf(STATUS_PENDING, STATUS_READY, STATUS_RETAKE, STATUS_APPROVED)
    }
}
