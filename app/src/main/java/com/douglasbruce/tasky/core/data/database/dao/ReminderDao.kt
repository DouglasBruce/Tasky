package com.douglasbruce.tasky.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.douglasbruce.tasky.core.data.database.model.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query(
        value = """
            SELECT * FROM reminders
            WHERE id = :reminderId
        """,
    )
    fun getReminderById(reminderId: String): Flow<ReminderEntity>

    @Query(
        value = """
            SELECT * FROM reminders
            WHERE time >= :startOfDate
            AND time < :endOfDate
        """,
    )
    fun getRemindersForDate(startOfDate: Long, endOfDate: Long): Flow<List<ReminderEntity>>
}