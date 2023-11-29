package com.douglasbruce.tasky.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.douglasbruce.tasky.core.data.database.dao.EventDao
import com.douglasbruce.tasky.core.data.database.dao.ReminderDao
import com.douglasbruce.tasky.core.data.database.dao.TaskDao
import com.douglasbruce.tasky.core.data.database.model.EventEntity
import com.douglasbruce.tasky.core.data.database.model.ModifiedAgendaItemEntity
import com.douglasbruce.tasky.core.data.database.model.ReminderEntity
import com.douglasbruce.tasky.core.data.database.model.TaskEntity
import com.douglasbruce.tasky.core.data.database.util.AgendaItemTypeConverter
import com.douglasbruce.tasky.core.data.database.util.AgendaPhotoConverter
import com.douglasbruce.tasky.core.data.database.util.AttendeeConverter
import com.douglasbruce.tasky.core.data.database.util.ModificationTypeConverter
import com.douglasbruce.tasky.core.data.database.util.NotificationTypeConverter

@Database(
    entities = [
        EventEntity::class,
        TaskEntity::class,
        ReminderEntity::class,
        ModifiedAgendaItemEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    NotificationTypeConverter::class,
    AttendeeConverter::class,
    ModificationTypeConverter::class,
    AgendaItemTypeConverter::class,
    AgendaPhotoConverter::class,
)
abstract class TaskyDatabase : RoomDatabase() {
    abstract fun eventsDao(): EventDao
    abstract fun tasksDao(): TaskDao
    abstract fun remindersDao(): ReminderDao
}