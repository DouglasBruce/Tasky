package com.douglasbruce.tasky.core.domain.repository

import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.network.model.NetworkAttendeeCheck

interface EventRepository {
    suspend fun getEventById(eventId: String): AuthResult<AgendaItem.Event>
    suspend fun createEvent(event: AgendaItem.Event): AuthResult<Unit>
    suspend fun updateEvent(
        event: AgendaItem.Event,
        deletedRemotePhotoKeys: List<String>,
    ): AuthResult<Unit>

    suspend fun deleteEventById(eventId: String): AuthResult<Unit>
    suspend fun getFutureEvents(): List<AgendaItem.Event>
    suspend fun getAttendee(email: String): AuthResult<NetworkAttendeeCheck>
    suspend fun leaveEvent(eventId: String): AuthResult<Unit>
}