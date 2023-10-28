package com.douglasbruce.tasky.core.data.database.di

import com.douglasbruce.tasky.core.data.database.TaskyDatabase
import com.douglasbruce.tasky.core.data.database.dao.EventDao
import com.douglasbruce.tasky.core.data.database.dao.ReminderDao
import com.douglasbruce.tasky.core.data.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesEventDao(
        database: TaskyDatabase,
    ): EventDao = database.eventsDao()

    @Provides
    fun providesTaskDao(
        database: TaskyDatabase,
    ): TaskDao = database.tasksDao()

    @Provides
    fun providesReminderDao(
        database: TaskyDatabase,
    ): ReminderDao = database.remindersDao()
}