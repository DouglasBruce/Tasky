package com.douglasbruce.tasky.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.douglasbruce.tasky.core.data.database.model.ModifiedAgendaItemEntity
import com.douglasbruce.tasky.core.data.database.model.ReminderEntity
import com.douglasbruce.tasky.core.model.ModificationType
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

@Dao
interface ReminderDao {
    @Upsert
    suspend fun upsertReminder(reminder: ReminderEntity)

    @Upsert
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
            WHERE time >= :startOfDate
            AND time < :endOfDate
        """,
    )
    fun getOneOffRemindersForDate(startOfDate: Long, endOfDate: Long): List<ReminderEntity>

    @Query(
        value = """
            SELECT * FROM reminders
            WHERE time >= :startingDate
        """
    )
    suspend fun getFutureReminders(
        startingDate: Long = ZonedDateTime.now().toInstant().toEpochMilli(),
    ): List<ReminderEntity>

    @Query(
        value = """
            DELETE FROM reminders
            WHERE id = :reminderId
        """,
    )
    suspend fun deleteReminderById(reminderId: String)

    @Upsert
    suspend fun upsertModifiedReminder(modifiedReminder: ModifiedAgendaItemEntity)

    @Query(
        value = """
            SELECT * FROM modified_agenda
            WHERE agendaItemType = 'Reminder'
            AND modificationType = :modificationType
        """,
    )
    suspend fun getModifiedRemindersWithModType(modificationType: ModificationType): List<ModifiedAgendaItemEntity>

    @Query(
        value = """
            DELETE FROM modified_agenda
            WHERE agendaItemType = 'Reminder'
            AND id = :id
            AND modificationType = :modificationType
        """,
    )
    suspend fun deleteModifiedReminderWithModType(id: String, modificationType: ModificationType)

    @Query(
        value = """
            DELETE FROM modified_agenda
            WHERE agendaItemType = 'Reminder'
            AND modificationType = :modificationType
        """,
    )
    suspend fun clearModifiedRemindersWithModType(modificationType: ModificationType)
}