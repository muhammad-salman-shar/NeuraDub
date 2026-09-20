package com.neurasamu.build.neura_dub.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val title: String,
    val language: String,
    val framerateMilli: Int,
    val loudnessTarget: String,
    val licenseConfirmed: Boolean,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long
) {
    companion object {
        const val STATUS_DRAFT = "draft"
        const val STATUS_ACTIVE = "active"
        const val STATUS_ARCHIVED = "archived"
    }
}
