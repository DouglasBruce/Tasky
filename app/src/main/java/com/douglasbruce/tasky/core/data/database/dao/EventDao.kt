package com.douglasbruce.tasky.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.douglasbruce.tasky.core.data.database.model.EventEntity

@Dao
interface EventDao {
    @Query(
        value = """
            SELECT * FROM events
            WHERE id = :eventId
        """,
    )
    fun getEventById(eventId: String): EventEntity
}