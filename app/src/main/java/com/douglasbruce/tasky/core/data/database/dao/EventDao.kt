package com.douglasbruce.tasky.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.douglasbruce.tasky.core.data.database.model.EventEntity
import com.douglasbruce.tasky.core.data.database.model.ModifiedAgendaItemEntity
import com.douglasbruce.tasky.core.model.ModificationType
import kotlinx.coroutines.flow.Flow
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
    suspend fun getEventById(eventId: String): EventEntity?

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
            WHERE `from` >= :startOfDate
            AND `from` < :endOfDate
        """,
    )
    suspend fun getOneOffEventsForDate(startOfDate: Long, endOfDate: Long): List<EventEntity>

    @Query(
        value = """
            SELECT * FROM events
            WHERE `from` >= :startingDate
        """,
    )
    suspend fun getFutureEvents(
        startingDate: Long = ZonedDateTime.now().toInstant().toEpochMilli(),
    ): List<EventEntity>

    @Query(
        value = """
            DELETE FROM events
            WHERE id = :eventId
        """,
    )
    suspend fun deleteEventById(eventId: String)

    @Query(
        value = """
            DELETE FROM events
            WHERE id = :eventId
        """,
    )
    suspend fun leaveEvent(eventId: String)

    @Upsert
    suspend fun upsertModifiedEvent(modifiedEvent: ModifiedAgendaItemEntity)

    @Query(
        value = """
            SELECT * FROM modified_agenda
            WHERE agendaItemType = 'Event'
            AND modificationType = :modificationType
        """,
    )
    suspend fun getModifiedEventsWithModType(modificationType: ModificationType): List<ModifiedAgendaItemEntity>

    @Query(
        value = """
            DELETE FROM modified_agenda
            WHERE agendaItemType = 'Event'
            AND id = :id
            AND modificationType = :modificationType
        """,
    )
    suspend fun deleteModifiedEventWithModType(id: String, modificationType: ModificationType)

    @Query(
        value = """
            DELETE FROM modified_agenda
            WHERE agendaItemType = 'Event'
            AND modificationType = :modificationType
        """,
    )
    suspend fun clearModifiedEventsWithModType(modificationType: ModificationType)
}