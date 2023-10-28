package com.douglasbruce.tasky.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.douglasbruce.tasky.core.data.database.model.ReminderEntity

@Dao
interface ReminderDao {
    @Query(
        value = """
            SELECT * FROM reminders
            WHERE id = :reminderId
        """,
    )
    fun getReminderById(reminderId: String): ReminderEntity
}