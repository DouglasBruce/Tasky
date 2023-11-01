package com.douglasbruce.tasky.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.douglasbruce.tasky.core.data.database.model.EventEntity
import kotlinx.coroutines.flow.Flow
import java.time.ZoneId
import java.time.ZonedDateTime

@Dao
interface EventDao {
    @Upsert
    suspend fun upsertEvent(event: EventEntity)

    @Upsert
    suspend fun upsertAllEvents(events: List<EventEntity>)

    @Query(
        value = """
            SELECT * FROM events
            WHERE id = :eventId
        """,
    )
    fun getEventById(eventId: String): EventEntity?

    @Query(
        value = """
            SELECT * FROM events
            WHERE `from` >= :startOfDate
            AND `from` < :endOfDate
        """,
    )
    fun getEventsForDate(startOfDate: Long, endOfDate: Long): Flow<List<EventEntity>>

    @Query(
        value = """
            SELECT * FROM events
            WHERE 'from' >= :startingDate
        """
    )
    suspend fun getFutureEvents(
        startingDate: Long = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"))
            .toEpochSecond() * 1000,
    ): List<EventEntity>

    @Query(
        value = """
            DELETE FROM events
            WHERE id = :eventId
        """,
    )
    suspend fun deleteEventById(eventId: String)
}