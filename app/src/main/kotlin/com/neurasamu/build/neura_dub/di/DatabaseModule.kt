package com.neurasamu.build.neura_dub.di

import android.content.Context
import androidx.room.Room
import com.neurasamu.build.neura_dub.data.local.NeuraDubDatabase
import com.neurasamu.build.neura_dub.data.local.ProjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): NeuraDubDatabase =
        Room.databaseBuilder(ctx, NeuraDubDatabase::class.java, "neura_dub.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideProjectDao(db: NeuraDubDatabase): ProjectDao = db.projectDao()
}
