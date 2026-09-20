package com.neurasamu.build.neura_dub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProjectEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NeuraDubDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}
