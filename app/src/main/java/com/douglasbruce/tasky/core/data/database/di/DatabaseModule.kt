package com.douglasbruce.tasky.core.data.database.di

import android.content.Context
import androidx.room.Room
import com.douglasbruce.tasky.core.data.database.TaskyDatabase
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
    fun providesTaskyDatabase(
        @ApplicationContext context: Context,
    ): TaskyDatabase = Room.databaseBuilder(
        context,
        TaskyDatabase::class.java,
        "tasky_database"
    ).build()
}