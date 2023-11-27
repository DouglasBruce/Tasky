package com.douglasbruce.tasky.core.domain.repository

import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.model.AgendaItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZonedDateTime

interface AgendaRepository {
    fun getAgendaForDate(date: LocalDate): Flow<List<AgendaItem>>
    suspend fun fetchAgendaForDate(date: LocalDate)
    suspend fun syncLocalDatabase(
        time: ZonedDateTime,
        updateSelectedDateOnly: Boolean,
    ): AuthResult<List<AgendaItem>>
}