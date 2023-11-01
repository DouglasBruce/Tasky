package com.douglasbruce.tasky.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.douglasbruce.tasky.core.data.database.model.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query(
        value = """
            SELECT * FROM events
            WHERE id = :eventId
        """,
    )
    fun getEventById(eventId: String): Flow<EventEntity>

    @Query(
        value = """
            SELECT * FROM events
            WHERE 'from' >= :date
            AND 'to' < :date
        """,
    )
    fun getEventsForDate(date: Long): Flow<List<EventEntity>>
}