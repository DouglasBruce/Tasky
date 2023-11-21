package com.douglasbruce.tasky.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.douglasbruce.tasky.core.data.database.dao.EventDao
import com.douglasbruce.tasky.core.data.database.dao.ReminderDao
import com.douglasbruce.tasky.core.data.database.dao.TaskDao
import com.douglasbruce.tasky.core.data.database.model.EventEntity
import com.douglasbruce.tasky.core.data.database.model.ModifiedEventEntity
import com.douglasbruce.tasky.core.data.database.model.ModifiedReminderEntity
import com.douglasbruce.tasky.core.data.database.model.ModifiedTaskEntity
import com.douglasbruce.tasky.core.data.database.model.ReminderEntity
import com.douglasbruce.tasky.core.data.database.model.TaskEntity
import com.douglasbruce.tasky.core.data.database.util.AttendeeConverter
import com.douglasbruce.tasky.core.data.database.util.ModificationTypeConverter
import com.douglasbruce.tasky.core.data.database.util.NotificationTypeConverter

@Database(
    entities = [
        EventEntity::class,
        ModifiedEventEntity::class,
        TaskEntity::class,
        ModifiedTaskEntity::class,
        ReminderEntity::class,
        ModifiedReminderEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    NotificationTypeConverter::class,
    AttendeeConverter::class,
    ModificationTypeConverter::class,
)
abstract class TaskyDatabase : RoomDatabase() {
    abstract fun eventsDao(): EventDao
    abstract fun tasksDao(): TaskDao
    abstract fun remindersDao(): ReminderDao
}