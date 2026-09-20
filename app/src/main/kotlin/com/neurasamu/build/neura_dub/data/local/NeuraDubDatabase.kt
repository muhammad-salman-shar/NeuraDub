package com.neurasamu.build.neura_dub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProjectEntity::class,
        CharacterEntity::class,
        SceneEntity::class,
        LineEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class NeuraDubDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun characterDao(): CharacterDao
    abstract fun sceneDao(): SceneDao
    abstract fun lineDao(): LineDao
}
