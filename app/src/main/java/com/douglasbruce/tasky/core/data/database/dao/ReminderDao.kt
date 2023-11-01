package com.douglasbruce.tasky.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.douglasbruce.tasky.core.data.database.model.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Upsert
    suspend fun upsertReminder(reminder: ReminderEntity)

    @Transaction
    suspend fun upsertAllReminders(reminders: List<ReminderEntity>)

    @Query(
        value = """
            SELECT * FROM reminders
            WHERE id = :reminderId
        """,
    )
    suspend fun getReminderById(reminderId: String): ReminderEntity?

    @Query(
        value = """
            SELECT * FROM reminders
            WHERE time >= :startOfDate
            AND time < :endOfDate
        """,
    )
    fun getRemindersForDate(startOfDate: Long, endOfDate: Long): Flow<List<ReminderEntity>>

    @Query(
        value = """
            SELECT * FROM reminders
            WHERE time >= :startingDate
        """
    )
    suspend fun getFutureReminders(startingDate: Long): List<ReminderEntity>

    @Query(
        value = """
            DELETE FROM reminders
            WHERE id = :reminderId
        """,
    )
    suspend fun deleteReminderById(reminderId: String)
}